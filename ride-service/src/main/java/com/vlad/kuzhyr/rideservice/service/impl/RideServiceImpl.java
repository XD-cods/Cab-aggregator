package com.vlad.kuzhyr.rideservice.service.impl;

import com.vlad.kuzhyr.rideservice.exception.RideNotFoundException;
import com.vlad.kuzhyr.rideservice.persistence.entity.Ride;
import com.vlad.kuzhyr.rideservice.persistence.entity.RideStatus;
import com.vlad.kuzhyr.rideservice.persistence.repository.RideRepository;
import com.vlad.kuzhyr.rideservice.service.RideService;
import com.vlad.kuzhyr.rideservice.utility.broker.RideEventProducer;
import com.vlad.kuzhyr.rideservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.rideservice.utility.mapper.PageResponseMapper;
import com.vlad.kuzhyr.rideservice.utility.mapper.RideMapper;
import com.vlad.kuzhyr.rideservice.utility.validation.RideValidation;
import com.vlad.kuzhyr.rideservice.web.dto.external.RideInfoPayload;
import com.vlad.kuzhyr.rideservice.web.dto.request.DriverAssignRequest;
import com.vlad.kuzhyr.rideservice.web.dto.request.RideRequest;
import com.vlad.kuzhyr.rideservice.web.dto.request.UpdateRideRequest;
import com.vlad.kuzhyr.rideservice.web.dto.request.UpdateRideStatusRequest;
import com.vlad.kuzhyr.rideservice.web.dto.response.PageResponse;
import com.vlad.kuzhyr.rideservice.web.dto.response.RideResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final RideMapper rideMapper;
    private final PageResponseMapper pageResponseMapper;
    private final AddressService addressService;
    private final RideValidation rideValidation;
    private final RideEventProducer rideEventProducer;

    @Override
    public RideResponse getRideById(Long rideId) {
        log.debug("getRideById: Entering method. Ride id: {}", rideId);

        Ride ride = findRideByIdOrThrow(rideId);

        log.info("getRideById: Ride found. Ride id: {}", rideId);
        return rideMapper.toResponse(ride);
    }

    @Override
    public PageResponse<RideResponse> getAllRidesByDriverId(Long driverId, Integer currentPage, Integer limit) {
        log.debug("getAllRidesByDriverId: Entering method. Driver id: {}, current page: {}, limit: {}", driverId,
            currentPage, limit);

        rideValidation.validateRidesExistByDriverId(driverId);
        Page<Ride> ridesPage = rideRepository.findByDriverId(driverId, PageRequest.of(currentPage, limit));

        PageResponse<RideResponse> pageResponse =
            pageResponseMapper.toPageResponse(
                ridesPage,
                currentPage,
                rideMapper::toResponse
            );

        log.info("getAllRidesByDriverId: Page of rides by driverId retrieved. Driver id: {}, {}",
            driverId, pageResponse);
        return pageResponse;
    }

    @Override
    public PageResponse<RideResponse> getAllRidesByPassengerId(Long passengerId, Integer currentPage, Integer limit) {
        log.debug("getAllRidesByPassengerId: Entering method. Passenger id: {}, current page: {}, limit: {}",
            passengerId, currentPage, limit);

        rideValidation.validateRidesExistByPassengerId(passengerId);
        Page<Ride> ridesPage = rideRepository.findByPassengerId(passengerId, PageRequest.of(currentPage, limit));

        PageResponse<RideResponse> pageResponse =
            pageResponseMapper.toPageResponse(ridesPage, currentPage, rideMapper::toResponse);

        log.info(
            "getAllRidesByPassengerId: Page of rides by passenger id retrieved. Passenger id: {}, {}",
            passengerId, pageResponse);
        return pageResponse;
    }

    @Override
    public PageResponse<RideResponse> getAllRides(Integer currentPage, Integer limit) {
        log.debug("getAllRides: Entering method. Current page: {}, limit: {}", currentPage, limit);

        Page<Ride> ridesPage = rideRepository.findAll(PageRequest.of(currentPage, limit));

        PageResponse<RideResponse> pageResponse =
            pageResponseMapper.toPageResponse(ridesPage, currentPage, rideMapper::toResponse);

        log.info("getAllRides: Page of rides retrieved. {}", pageResponse);
        return pageResponse;
    }

    @Override
    @Transactional
    public RideResponse updateRide(Long rideId, UpdateRideRequest rideRequest) {
        log.debug("updateRide: Entering method. Ride id: {}, {}", rideId, rideRequest);

        Ride ride = findRideByIdOrThrow(rideId);
        rideValidation.validateRideIsUpdatable(ride);

        updateRideAddressIfNeeded(ride, rideRequest);

        Ride updatedRide = rideRepository.save(ride);

        log.info("updateRide: Ride updated. Ride id: {}", rideId);
        return rideMapper.toResponse(updatedRide);
    }

    @Override
    @Transactional
    public RideResponse updateRideStatus(Long rideId, UpdateRideStatusRequest rideRequest) {
        log.debug("updateRideStatus: Entering method. Ride id: {}, {}", rideId, rideRequest);

        Ride ride = findRideByIdOrThrow(rideId);
        RideStatus newStatus = rideRequest.rideStatus();

        rideValidation.validateStatusTransition(ride.getRideStatus(), newStatus);

        updateRideTimestamps(ride, newStatus);
        ride.setRideStatus(newStatus);

        Ride updatedRide = rideRepository.save(ride);

        log.info("updateRideStatus: Ride status updated. Ride id: {}, new status: {}", rideId, newStatus);
        return rideMapper.toResponse(updatedRide);
    }

    @Override
    @Transactional
    public RideResponse createRide(RideRequest rideRequest) {
        log.debug("createRide: Entering method. {}", rideRequest);

        Ride ride = createRideFromRideRequest(rideRequest);
        Ride savedRide = rideRepository.save(ride);

        log.info("createRide: Ride created. Ride id: {}", savedRide.getId());
        return rideMapper.toResponse(savedRide);
    }

    @Override
    public RideResponse assignDriver(DriverAssignRequest driverAssignRequest, Long rideId) {
        log.debug("assignDriver: Entering method. {}", driverAssignRequest);

        Long driverId = driverAssignRequest.driverId();
        Ride ride = assignDriverByRide(rideId, driverId);

        Ride savedRide = rideRepository.save(ride);

        log.info("assignDriver: Driver assigned. Ride id: {}, driver id: {}", rideId, driverAssignRequest);

        return rideMapper.toResponse(savedRide);

    }

    private Ride assignDriverByRide(Long rideId, Long driverId) {
        Ride ride = findRideByIdOrThrow(rideId);

        rideValidation.checkDriverAvailability(driverId);
        ride.setDriverId(driverId);
        ride.setRideStatus(RideStatus.DRIVER_ASSIGNED);
        setDriverBusyStatus(driverId, true);

        return ride;
    }

    private Ride findRideByIdOrThrow(Long rideId) {
        log.debug("findRideByIdOrThrow: Attempting to find ride. Ride id: {}", rideId);

        return rideRepository.findById(rideId)
            .orElseThrow(() -> {
                log.error("findRideByIdOrThrow: Ride not found. Ride id: {}", rideId);
                return new RideNotFoundException(
                    ExceptionMessageConstant.RIDE_NOT_FOUND_MESSAGE.formatted(rideId)
                );
            });
    }

    private void updateRideAddressIfNeeded(Ride ride, UpdateRideRequest rideRequest) {
        String rideRequestDepartureAddress = rideRequest.departureAddress().trim();
        String rideRequestDestinationAddress = rideRequest.destinationAddress().trim();

        log.debug("updateRideAddressIfNeeded: Updating ride address. Ride id: {}", ride.getId());
        addressService.updateRideAddress(ride, rideRequestDepartureAddress, rideRequestDestinationAddress);
    }

    private Ride createRideFromRideRequest(RideRequest rideRequest) {
        Long passengerId = rideRequest.passengerId();

        rideValidation.checkPassengerAvailability(passengerId);

        String departureAddress = rideRequest.departureAddress();
        String destinationAddress = rideRequest.destinationAddress();

        addressService.validateDepartureAndDestinationDifferentAddresses(departureAddress, destinationAddress);

        Ride ride = rideMapper.toEntity(rideRequest);
        addressService.updateRideAddress(ride, departureAddress, destinationAddress);
        setPassengerBusyStatus(passengerId, true);

        return ride;
    }

    private void updateRideTimestamps(Ride ride, RideStatus newStatus) {
        switch (newStatus) {
            case PASSENGER_PICKED_UP -> {
                ride.setPickupTime(LocalDateTime.now());
                log.debug("updateRideTimestamps: Passenger picked up. Ride id: {}", ride.getId());
            }
            case COMPLETED -> {
                ride.setCompleteTime(LocalDateTime.now());
                setDriverAndPassengerBusyStatus(ride.getPassengerId(), ride.getDriverId(), false);
                log.debug("updateRideTimestamps: Ride completed. Ride id: {}", ride.getId());
            }
            case RATE -> {
                rideEventProducer.sendRideCompletedMessage(
                    RideInfoPayload.builder()
                        .rideId(ride.getId())
                        .passengerId(ride.getPassengerId())
                        .driverId(ride.getDriverId())
                        .build()
                );
                log.debug("updateRideTimestamps: Ride rated. Ride id: {}", ride.getId());
            }
            case CANCELLED -> {
                if (ride.getPassengerId() != null) {
                    setPassengerBusyStatus(ride.getPassengerId(), false);
                }

                if (ride.getDriverId() != null) {
                    setDriverBusyStatus(ride.getPassengerId(), false);
                }
                log.debug("updateRideTimestamps: Ride cancelled. Ride id: {}", ride.getId());
            }
            default -> {
            }
        }
    }

    private void setPassengerBusyStatus(Long passengerId, Boolean isBusy) {
        log.debug("setPassengerBusyStatus: Setting passenger busy status. Passenger id: {}, isBusy: {}",
            passengerId, isBusy);

        rideEventProducer.sendPassengerBusyTopic(passengerId, isBusy);
    }

    private void setDriverBusyStatus(Long driverId, Boolean isBusy) {
        log.debug("setDriverBusyStatus: Setting driver busy status. Driver id: {}, isBusy: {}",
            driverId, isBusy);

        rideEventProducer.sendDriverBusyMessage(driverId, isBusy);
    }

    private void setDriverAndPassengerBusyStatus(Long passengerId, Long driverId, Boolean isBusy) {
        setPassengerBusyStatus(passengerId, isBusy);
        setDriverBusyStatus(driverId, isBusy);
    }
}
