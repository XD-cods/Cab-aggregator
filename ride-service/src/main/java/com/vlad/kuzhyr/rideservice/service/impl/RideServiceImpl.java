package com.vlad.kuzhyr.rideservice.service.impl;

import com.vlad.kuzhyr.rideservice.exception.NotValidStatusTransitionException;
import com.vlad.kuzhyr.rideservice.exception.RideNotFoundException;
import com.vlad.kuzhyr.rideservice.persistence.entity.Ride;
import com.vlad.kuzhyr.rideservice.persistence.entity.RideStatus;
import com.vlad.kuzhyr.rideservice.persistence.repository.RideRepository;
import com.vlad.kuzhyr.rideservice.service.RideService;
import com.vlad.kuzhyr.rideservice.utility.client.DriverFeignClient;
import com.vlad.kuzhyr.rideservice.utility.client.PassengerFeignClient;
import com.vlad.kuzhyr.rideservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.rideservice.utility.mapper.PageResponseMapper;
import com.vlad.kuzhyr.rideservice.utility.mapper.RideMapper;
import com.vlad.kuzhyr.rideservice.utility.validation.RideValidation;
import com.vlad.kuzhyr.rideservice.web.request.RideRequest;
import com.vlad.kuzhyr.rideservice.web.request.UpdateRideRequest;
import com.vlad.kuzhyr.rideservice.web.request.UpdateRideStatusRequest;
import com.vlad.kuzhyr.rideservice.web.response.PageResponse;
import com.vlad.kuzhyr.rideservice.web.response.RideResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;

    private final RideMapper rideMapper;

    private final PageResponseMapper pageResponseMapper;

    private final AddressService addressService;

    private final RideValidation rideValidation;

    private final PassengerFeignClient passengerFeignClient;

    private final DriverFeignClient driverFeignClient;

    @Override
    public RideResponse getRideById(Long rideId) {
        Ride ride = findRideByIdOrThrow(rideId);
        return rideMapper.toResponse(ride);
    }

    @Override
    public PageResponse<RideResponse> getAllRidesByDriverId(Long driverId, Integer currentPage, Integer limit) {
        rideValidation.validateRidesExistByDriverId(driverId);
        Page<Ride> ridesPage = rideRepository.findByDriverId(driverId, PageRequest.of(currentPage, limit));
        return pageResponseMapper.toPageResponse(ridesPage, currentPage, rideMapper::toResponse);
    }

    @Override
    public PageResponse<RideResponse> getAllRidesByPassengerId(Long passengerId, Integer currentPage, Integer limit) {
        rideValidation.validateRidesExistByPassengerId(passengerId);
        Page<Ride> ridesPage = rideRepository.findByPassengerId(passengerId, PageRequest.of(currentPage, limit));
        return pageResponseMapper.toPageResponse(ridesPage, currentPage, rideMapper::toResponse);
    }

    @Override
    public PageResponse<RideResponse> getAllRides(Integer currentPage, Integer limit) {
        Page<Ride> ridesPage = rideRepository.findAll(PageRequest.of(currentPage, limit));
        return pageResponseMapper.toPageResponse(ridesPage, currentPage, rideMapper::toResponse);
    }

    @Override
    @Transactional
    public RideResponse updateRide(Long rideId, UpdateRideRequest rideRequest) {
        Ride ride = findRideByIdOrThrow(rideId);
        rideValidation.validateRideIsUpdatable(ride);

        updateRideAddressIfNeeded(ride, rideRequest);

        Ride updatedRide = rideRepository.save(ride);
        return rideMapper.toResponse(updatedRide);
    }

    @Override
    @Transactional
    public RideResponse updateRideStatus(Long rideId, UpdateRideStatusRequest rideRequest) {
        Ride ride = findRideByIdOrThrow(rideId);
        RideStatus newStatus = rideRequest.rideStatus();

        validateStatusTransition(ride.getRideStatus(), newStatus);

        updateRideTimestamps(ride, newStatus);
        ride.setRideStatus(newStatus);

        Ride updatedRide = rideRepository.save(ride);
        return rideMapper.toResponse(updatedRide);
    }

    @Override
    @Transactional
    public RideResponse createRide(RideRequest rideRequest) {
        Ride ride = createRideFromRideRequest(rideRequest);
        Ride savedRide = rideRepository.save(ride);
        return rideMapper.toResponse(savedRide);
    }

    private Ride findRideByIdOrThrow(Long rideId) {
        return rideRepository.findById(rideId)
            .orElseThrow(() -> new RideNotFoundException(
                ExceptionMessageConstant.RIDE_NOT_FOUND_MESSAGE.formatted(rideId)
            ));
    }

    private void updateRideAddressIfNeeded(Ride ride, UpdateRideRequest rideRequest) {
        String currentDepartureAddress = ride.getDepartureAddress().getAddressName().trim();
        String currentDestinationAddress = ride.getDestinationAddress().getAddressName().trim();
        String rideRequestDepartureAddress = rideRequest.departureAddress().trim();
        String rideRequestDestinationAddress = rideRequest.destinationAddress().trim();

        if (!currentDepartureAddress.equals(rideRequestDepartureAddress)
            || !currentDestinationAddress.equals(rideRequestDestinationAddress)) {
            addressService.updateRideAddress(ride, rideRequestDepartureAddress, rideRequestDestinationAddress);
        }
    }

    private Ride createRideFromRideRequest(RideRequest rideRequest) {
        Long passengerId = rideRequest.passengerId();
        Long driverId = rideRequest.driverId();

        ensurePassengerExists(passengerId);
        ensureDriverExists(driverId);

        String rideRequestDepartureAddress = rideRequest.departureAddress();
        String rideRequestDestinationAddress = rideRequest.destinationAddress();

        addressService.validateDifferentAddresses(rideRequestDepartureAddress, rideRequestDestinationAddress);

        Ride ride = rideMapper.toEntity(rideRequest);
        addressService.updateRideAddress(ride, rideRequestDepartureAddress, rideRequestDestinationAddress);

        return ride;
    }

    private void validateStatusTransition(RideStatus currentStatus, RideStatus newStatus) {
        if (!currentStatus.isTransitionAllowed(newStatus)) {
            throw new NotValidStatusTransitionException(
                ExceptionMessageConstant.NOT_VALID_STATUS_TRANSITION.formatted(currentStatus, newStatus)
            );
        }
    }

    private void updateRideTimestamps(Ride ride, RideStatus newStatus) {
        switch (newStatus) {
            case PASSENGER_PICKED_UP -> ride.setPickupTime(LocalDateTime.now());
            case COMPLETED -> ride.setCompleteTime(LocalDateTime.now());
            default -> throw new IllegalStateException(
                ExceptionMessageConstant.STATUS_NOT_FOUND.formatted(newStatus)
            );
        }
    }

    private void ensurePassengerExists(Long passengerId) {
        passengerFeignClient.getPassengerById(passengerId);
    }

    private void ensureDriverExists(Long driverId) {
        driverFeignClient.getDriverById(driverId);
    }

}
