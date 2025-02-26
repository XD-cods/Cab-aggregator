package com.vlad.kuzhyr.passengerservice.utility.broker;

import com.vlad.kuzhyr.passengerservice.exception.PassengerNotFoundException;
import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import com.vlad.kuzhyr.passengerservice.persistence.repository.PassengerRepository;
import com.vlad.kuzhyr.passengerservice.utility.constant.ExceptionMessageConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PassengerProcessor {

    private final PassengerRepository passengerRepository;

    public void updatePassengerByIdAndIsBusy(Long passengerId, boolean isBusy) {
        log.debug("updatePassengerByIdAndIsBusy: Entering method. Passenger id: {}, isBusy: {}",
            passengerId, isBusy);

        Passenger passenger = getPassengerById(passengerId);

        passenger.setIsBusy(isBusy);
        Passenger savedPassenger = passengerRepository.save(passenger);

        log.info("updatePassengerByIdAndIsBusy: Passenger updated successfully. Passenger id: {}, new isBusy: {}",
            savedPassenger.getId(), savedPassenger.getIsBusy());
    }

    public Passenger getPassengerById(Long passengerId) {
        log.debug("getPassengerById: Attempting to find passenger. Passenger id: {}", passengerId);

        return passengerRepository.findById(passengerId)
            .orElseThrow(() -> {
                log.error("getPassengerById: Passenger not found. Passenger id: {}", passengerId);
                return new PassengerNotFoundException(
                    ExceptionMessageConstant.PASSENGER_NOT_FOUND_MESSAGE.formatted(passengerId)
                );
            });
    }
}
