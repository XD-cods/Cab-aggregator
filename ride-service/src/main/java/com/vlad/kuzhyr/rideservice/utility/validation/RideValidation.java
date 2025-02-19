package com.vlad.kuzhyr.rideservice.utility.validation;

import com.vlad.kuzhyr.rideservice.exception.DriverHasNotCarException;
import com.vlad.kuzhyr.rideservice.exception.DriverIsBusyException;
import com.vlad.kuzhyr.rideservice.exception.PassengerIsBusyException;
import com.vlad.kuzhyr.rideservice.exception.RideCanNotUpdatableException;
import com.vlad.kuzhyr.rideservice.exception.RidesNotFoundByDriverIdException;
import com.vlad.kuzhyr.rideservice.exception.RidesNotFoundByPassengerIdException;
import com.vlad.kuzhyr.rideservice.persistence.entity.Ride;
import com.vlad.kuzhyr.rideservice.persistence.repository.RideRepository;
import com.vlad.kuzhyr.rideservice.utility.client.DriverFeignClient;
import com.vlad.kuzhyr.rideservice.utility.client.PassengerFeignClient;
import com.vlad.kuzhyr.rideservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.rideservice.web.dto.external.DriverResponse;
import com.vlad.kuzhyr.rideservice.web.dto.external.PassengerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RideValidation {

    private final RideRepository rideRepository;
    private final PassengerFeignClient passengerFeignClient;
    private final DriverFeignClient driverFeignClient;

    public void validateRidesExistByDriverId(Long driverId) {
        if (!rideRepository.existsRidesByDriverId(driverId)) {
            throw new RidesNotFoundByDriverIdException(
                ExceptionMessageConstant.RIDES_NOT_FOUND_BY_DRIVER_ID_MESSAGE.formatted(driverId)
            );
        }
    }

    public void validateRidesExistByPassengerId(Long passengerId) {
        if (!rideRepository.existsRidesByPassengerId(passengerId)) {
            throw new RidesNotFoundByPassengerIdException(
                ExceptionMessageConstant.RIDES_NOT_FOUND_BY_PASSENGER_ID_MESSAGE.formatted(passengerId)
            );
        }
    }

    public void validateRideIsUpdatable(Ride ride) {
        if (!ride.getRideStatus().isUpdatable()) {
            throw new RideCanNotUpdatableException(
                ExceptionMessageConstant.RIDE_CAN_NOT_UPDATABLE_MESSAGE.formatted(ride.getRideStatus())
            );
        }
    }

    public void checkDriverAndPassengerAvailability(Long driverId, Long passengerId) {
        DriverResponse driverResponse = getDriverByDriverId(driverId);
        PassengerResponse passengerResponse = getPassengerByPassengerId(passengerId);

        if (driverResponse.isBusy()) {
            throw new DriverIsBusyException(
                ExceptionMessageConstant.DRIVER_BUSY_MESSAGE.formatted(driverId)
            );
        }

        if (passengerResponse.isBusy()) {
            throw new PassengerIsBusyException(
                ExceptionMessageConstant.PASSENGER_BUSY_MESSAGE.formatted(passengerId)
            );
        }

        if (driverResponse.carIds().isEmpty()) {
            throw new DriverHasNotCarException(
                ExceptionMessageConstant.DRIVER_HAS_NO_CAR.formatted(driverId)
            );
        }
    }

    private PassengerResponse getPassengerByPassengerId(Long passengerId) {
        PassengerResponse passengerResponse = passengerFeignClient.getPassengerById(passengerId).getBody();
        return passengerResponse;
    }

    private DriverResponse getDriverByDriverId(Long driverId) {
        DriverResponse driverResponse = driverFeignClient.getDriverById(driverId).getBody();
        return driverResponse;
    }
}
