package com.vlad.kuzhyr.rideservice.utility.validation;

import com.vlad.kuzhyr.rideservice.exception.DriverHasNotCarException;
import com.vlad.kuzhyr.rideservice.exception.DriverIsBusyException;
import com.vlad.kuzhyr.rideservice.exception.NotValidStatusTransitionException;
import com.vlad.kuzhyr.rideservice.exception.PassengerIsBusyException;
import com.vlad.kuzhyr.rideservice.exception.RideCanNotUpdatableException;
import com.vlad.kuzhyr.rideservice.exception.RidesNotFoundByDriverIdException;
import com.vlad.kuzhyr.rideservice.exception.RidesNotFoundByPassengerIdException;
import com.vlad.kuzhyr.rideservice.persistence.entity.Ride;
import com.vlad.kuzhyr.rideservice.persistence.entity.RideStatus;
import com.vlad.kuzhyr.rideservice.persistence.repository.RideRepository;
import com.vlad.kuzhyr.rideservice.utility.client.DriverFeignClient;
import com.vlad.kuzhyr.rideservice.utility.client.PassengerFeignClient;
import com.vlad.kuzhyr.rideservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.rideservice.web.dto.external.DriverResponse;
import com.vlad.kuzhyr.rideservice.web.dto.external.PassengerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RideValidation {

    private final RideRepository rideRepository;
    private final PassengerFeignClient passengerFeignClient;
    private final DriverFeignClient driverFeignClient;

    public void validateRidesExistByDriverId(Long driverId) {
        log.info("validateRidesExistByDriverId: Validate rides exist by driver id. Driver id: {}", driverId);
        if (!rideRepository.existsRidesByDriverId(driverId)) {
            log.error("validateRidesExistByDriverId: Rides not found by driver id. Driver id: {}", driverId);
            throw new RidesNotFoundByDriverIdException(
                ExceptionMessageConstant.RIDES_NOT_FOUND_BY_DRIVER_ID_MESSAGE.formatted(driverId)
            );
        }
        log.debug("validateRidesExistByDriverId: Rides exist by driver id. Driver id: {}", driverId);
    }

    public void validateRidesExistByPassengerId(Long passengerId) {
        log.info("validateRidesExistByPassengerId: Validate rides exist by passenger id. Passenger id: {}",
            passengerId);
        if (!rideRepository.existsRidesByPassengerId(passengerId)) {
            log.error("validateRidesExistByPassengerId: Rides not found by passenger id. Passenger id: {}",
                passengerId);
            throw new RidesNotFoundByPassengerIdException(
                ExceptionMessageConstant.RIDES_NOT_FOUND_BY_PASSENGER_ID_MESSAGE.formatted(passengerId)
            );
        }

        log.debug("validateRidesExistByPassengerId: Rides exists by passenger id. Passenger id: {}",
            passengerId);
    }

    public void validateRideIsUpdatable(Ride ride) {
        log.debug("validateRideIsUpdatable: Validate ride is updatable. Ride id: {}, current status: {}", ride.getId(),
            ride.getRideStatus());
        if (!ride.getRideStatus().isUpdatable()) {
            log.error("validateRideIsUpdatable: Ride is not updatable. Ride id: {}, current status: {}", ride.getId(),
                ride.getRideStatus());
            throw new RideCanNotUpdatableException(
                ExceptionMessageConstant.RIDE_CAN_NOT_UPDATABLE_MESSAGE.formatted(ride.getRideStatus())
            );
        }
    }

    public void checkDriverAndPassengerAvailability(Long driverId, Long passengerId) {
        log.info(
            "checkDriverAndPassengerAvailability: Check driver and passenger availability. Driver id: {}, " +
                "passenger id: {}",
            driverId,
            passengerId);

        checkDriverAvailability(driverId);
        checkPassengerAvailability(passengerId);
    }

    public void checkDriverAvailability(Long driverId) {
        log.info(
            "checkDriverAvailability: Check driver availability. Driver id: {}", driverId);
        DriverResponse driverResponse = getDriverByDriverId(driverId);

        if (driverResponse.isBusy()) {
            log.error("checkDriverAvailability: Driver busy. Driver id: {}", driverId);
            throw new DriverIsBusyException(
                ExceptionMessageConstant.DRIVER_BUSY_MESSAGE.formatted(driverId)
            );
        }

        if (driverResponse.carIds().isEmpty()) {
            log.error("checkDriverAvailability: Driver has no cars. Driver id: {}", driverId);
            throw new DriverHasNotCarException(
                ExceptionMessageConstant.DRIVER_HAS_NO_CAR.formatted(driverId)
            );
        }

        log.info("checkDriverAvailability: Driver valid by. Driver id: {}", driverId);
    }

    public void checkPassengerAvailability(Long passengerId) {
        log.info("checkPassengerAvailability: Check passenger availability. Passenger id: {}", passengerId);

        PassengerResponse passengerResponse = getPassengerByPassengerId(passengerId);

        if (passengerResponse.isBusy()) {
            log.error("checkPassengerAvailability: Passenger busy. Passenger id: {}", passengerId);
            throw new PassengerIsBusyException(
                ExceptionMessageConstant.PASSENGER_BUSY_MESSAGE.formatted(passengerId)
            );
        }

        log.info("checkPassengerAvailability: Passenger valid by passenger id. Passenger id: {}", passengerId);
    }

    public void validateStatusTransition(RideStatus currentStatus, RideStatus newStatus) {
        log.debug("validateStatusTransition: Validate status transition. Current status: {}, new status: {}",
            currentStatus,
            newStatus);
        if (!currentStatus.isTransitionAllowed(newStatus)) {
            log.error("validateStatusTransition: Invalid status transition. Current status: {}, new status: {}",
                currentStatus,
                newStatus);
            throw new NotValidStatusTransitionException(
                ExceptionMessageConstant.NOT_VALID_STATUS_TRANSITION.formatted(currentStatus, newStatus)
            );
        }
    }

    private PassengerResponse getPassengerByPassengerId(Long passengerId) {
        PassengerResponse passengerResponse = null;
        try {
            passengerResponse = passengerFeignClient.getPassengerById(passengerId).getBody();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.debug("getPassengerByPassengerId: Retrieved passenger by id. Passenger id: {}", passengerId);
        return passengerResponse;
    }

    private DriverResponse getDriverByDriverId(Long driverId) {
        DriverResponse driverResponse = driverFeignClient.getDriverById(driverId).getBody();
        log.debug("Ride validation: Retrieved driver by id. Driver id: {}", driverId);
        return driverResponse;
    }

}
