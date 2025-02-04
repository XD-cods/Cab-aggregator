package com.vlad.kuzhyr.rideservice.utility.validation;

import com.vlad.kuzhyr.rideservice.exception.RideCanNotUpdatableException;
import com.vlad.kuzhyr.rideservice.exception.RidesNotFoundByDriverIdException;
import com.vlad.kuzhyr.rideservice.exception.RidesNotFoundByPassengerIdException;
import com.vlad.kuzhyr.rideservice.persistence.entity.Ride;
import com.vlad.kuzhyr.rideservice.persistence.repository.RideRepository;
import com.vlad.kuzhyr.rideservice.utility.constant.ExceptionMessageConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RideValidation {

    private final RideRepository rideRepository;

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

}
