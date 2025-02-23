package com.vlad.kuzhyr.rideservice.service.impl;

import com.vlad.kuzhyr.rideservice.exception.RideNotFoundException;
import com.vlad.kuzhyr.rideservice.persistence.entity.Ride;
import com.vlad.kuzhyr.rideservice.persistence.entity.RideStatus;
import com.vlad.kuzhyr.rideservice.persistence.repository.RideRepository;
import com.vlad.kuzhyr.rideservice.service.RideService;
import com.vlad.kuzhyr.rideservice.utility.broker.KafkaProducer;
import com.vlad.kuzhyr.rideservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.rideservice.utility.mapper.PageResponseMapper;
import com.vlad.kuzhyr.rideservice.utility.mapper.RideMapper;
import com.vlad.kuzhyr.rideservice.utility.validation.RideValidation;
import com.vlad.kuzhyr.rideservice.web.dto.external.RideInfoPayload;
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
    private final KafkaProducer kafkaProducer;

    @Override
    public RideResponse getRideById(Long rideId) {
        log.debug("Ride service. Get ride by id. Ride id: {}", rideId);
        Ride ride = findRideByIdOrThrow(rideId);
        log.info("Ride service. Retrieved ride by id. Ride id: {}", rideId);
        return rideMapper.toResponse(ride);
    }

    @Override
    public PageResponse<RideResponse> getAllRidesByDriverId(Long driverId, Integer currentPage, Integer limit) {
        log.debug("Ride service. Get all rides by driver id. Driver id: {}", driverId);
        rideValidation.validateRidesExistByDriverId(driverId);
        Page<Ride> ridesPage = rideRepository.findByDriverId(driverId, PageRequest.of(currentPage, limit));
        log.info("Ride service. Retrieved all rides by driver id. Driver id: {}", driverId);
        return pageResponseMapper.toPageResponse(ridesPage, currentPage, rideMapper::toResponse);
    }

    @Override
    public PageResponse<RideResponse> getAllRidesByPassengerId(Long passengerId, Integer currentPage, Integer limit) {
        log.debug("Ride service. Get all rides by passenger id. Passenger id: {}", passengerId);
        rideValidation.validateRidesExistByPassengerId(passengerId);
        Page<Ride> ridesPage = rideRepository.findByPassengerId(passengerId, PageRequest.of(currentPage, limit));
        log.info("Ride service. Retrieved all rides by passenger id. Passenger id: {}", passengerId);
        return pageResponseMapper.toPageResponse(ridesPage, currentPage, rideMapper::toResponse);
    }

    @Override
    public PageResponse<RideResponse> getAllRides(Integer currentPage, Integer limit) {
        log.debug("Ride service. Get all rides. Current page: {}, limit: {}", currentPage, limit);
        Page<Ride> ridesPage = rideRepository.findAll(PageRequest.of(currentPage, limit));
        log.info("Ride service. Retrieved all rides. Current page: {}, total pages: {}", currentPage,
            ridesPage.getTotalPages());
        return pageResponseMapper.toPageResponse(ridesPage, currentPage, rideMapper::toResponse);
    }

    @Override
    @Transactional
    public RideResponse updateRide(Long rideId, UpdateRideRequest rideRequest) {
        log.debug("Ride service. Update ride. Ride id: {}", rideId);
        Ride ride = findRideByIdOrThrow(rideId);
        rideValidation.validateRideIsUpdatable(ride);

        updateRideAddressIfNeeded(ride, rideRequest);

        Ride updatedRide = rideRepository.save(ride);
        log.info("Ride service. Updated ride. Ride id: {}", rideId);
        return rideMapper.toResponse(updatedRide);
    }

    @Override
    @Transactional
    public RideResponse updateRideStatus(Long rideId, UpdateRideStatusRequest rideRequest) {
        log.debug("Ride service. Update ride status. Ride id: {}", rideId);
        Ride ride = findRideByIdOrThrow(rideId);
        RideStatus newStatus = rideRequest.rideStatus();

        rideValidation.validateStatusTransition(ride.getRideStatus(), newStatus);

        updateRideTimestamps(ride, newStatus);
        ride.setRideStatus(newStatus);

        Ride updatedRide = rideRepository.save(ride);
        log.info("Ride service. Updated ride status. Ride id: {}, new status: {}", rideId, newStatus);
        return rideMapper.toResponse(updatedRide);
    }

    @Override
    @Transactional
    public RideResponse createRide(RideRequest rideRequest) {
        log.debug("Ride service. Create ride. Passenger id: {}, driver id: {}", rideRequest.passengerId(),
            rideRequest.driverId());
        Ride ride = createRideFromRideRequest(rideRequest);
        Ride savedRide = rideRepository.save(ride);
        log.info("Ride service. Created ride. Ride id: {}", savedRide.getId());
        return rideMapper.toResponse(savedRide);
    }

    private Ride findRideByIdOrThrow(Long rideId) {
        log.debug("Ride service. Attempting to find ride by id. Ride id: {}", rideId);
        return rideRepository.findById(rideId)
            .orElseThrow(() -> {
                log.error("Ride service. Ride not found. Ride id: {}", rideId);
                return new RideNotFoundException(
                    ExceptionMessageConstant.RIDE_NOT_FOUND_MESSAGE.formatted(rideId)
                );
            });
    }

    private void updateRideAddressIfNeeded(Ride ride, UpdateRideRequest rideRequest) {
        String currentDepartureAddress = ride.getDepartureAddress().getAddressName().trim();
        String currentDestinationAddress = ride.getDestinationAddress().getAddressName().trim();
        String rideRequestDepartureAddress = rideRequest.departureAddress().trim();
        String rideRequestDestinationAddress = rideRequest.destinationAddress().trim();

        if (!currentDepartureAddress.equals(rideRequestDepartureAddress)
            || !currentDestinationAddress.equals(rideRequestDestinationAddress)) {
            log.debug("Ride service. Updating ride address. Ride id: {}", ride.getId());
            addressService.updateRideAddress(ride, rideRequestDepartureAddress, rideRequestDestinationAddress);
        }
    }

    private Ride createRideFromRideRequest(RideRequest rideRequest) {
        Long passengerId = rideRequest.passengerId();
        Long driverId = rideRequest.driverId();

        rideValidation.checkDriverAndPassengerAvailability(driverId, passengerId);

        String departureAddress = rideRequest.departureAddress();
        String destinationAddress = rideRequest.destinationAddress();

        addressService.validateDifferentAddresses(departureAddress, destinationAddress);

        Ride ride = rideMapper.toEntity(rideRequest);
        addressService.updateRideAddress(ride, departureAddress, destinationAddress);
        setDriverAndPassengerBusyStatus(passengerId, driverId, true);
        return ride;
    }

    private void updateRideTimestamps(Ride ride, RideStatus newStatus) {
        switch (newStatus) {
            case PASSENGER_PICKED_UP -> {
                ride.setPickupTime(LocalDateTime.now());
                log.debug("Ride service. Passenger picked up. Ride id: {}", ride.getId());
            }
            case COMPLETED -> {
                ride.setCompleteTime(LocalDateTime.now());
                setDriverAndPassengerBusyStatus(ride.getPassengerId(), ride.getDriverId(), false);
                log.debug("Ride service. Ride completed. Ride id: {}", ride.getId());
            }
            case RATE -> {
                kafkaProducer.sendRideCompletedMessage(
                    RideInfoPayload.builder()
                        .rideId(ride.getId())
                        .passengerId(ride.getPassengerId())
                        .driverId(ride.getDriverId())
                        .build()
                );
                log.debug("Ride service. Ride rated. Ride id: {}", ride.getId());
            }
            case CANCELLED -> {
                setDriverAndPassengerBusyStatus(ride.getPassengerId(), ride.getDriverId(), false);
                log.debug("Ride service. Ride cancelled. Ride id: {}", ride.getId());
            }
            default -> {
            }
        }
    }

    private void setDriverAndPassengerBusyStatus(Long passengerId, Long driverId, Boolean isBusy) {
        log.debug("Ride service. Setting driver and passenger busy status. Driver id: {}, passenger id: {}, isBusy: {}",
            driverId, passengerId, isBusy);
        kafkaProducer.sendDriverBusyMessage(driverId, isBusy);
        kafkaProducer.sendPassengerBusyTopic(passengerId, isBusy);
    }

}