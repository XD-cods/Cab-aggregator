package com.vlad.kuzhyr.passengerservice.utility.validator;

import com.vlad.kuzhyr.passengerservice.exception.PassengerAlreadyExistsException;
import com.vlad.kuzhyr.passengerservice.persistence.repository.PassengerRepository;
import com.vlad.kuzhyr.passengerservice.utility.constant.ExceptionMessageConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PassengerValidator {

    private final PassengerRepository passengerRepository;

    public void validatePassengerEmailAndPhone(String passengerRequestEmail, String passengerRequestPhone) {
        log.debug("Passenger validator. Validate passenger email. Passenger email: {}", passengerRequestEmail);

        if (passengerRepository.existsPassengerByEmailAndIsEnabledTrue(passengerRequestEmail)) {
            log.error("Passenger validator. Passenger already exists by email. Passenger email: {}",
                passengerRequestEmail);
            throw new PassengerAlreadyExistsException(
                ExceptionMessageConstant.PASSENGER_ALREADY_EXISTS_BY_EMAIL_MESSAGE.formatted(passengerRequestEmail)
            );
        }

        log.debug("Passenger validator. Validate passenger phone. Passenger phone: {}", passengerRequestPhone);

        if (passengerRepository.existsPassengerByPhoneAndIsEnabledTrue(passengerRequestPhone)) {
            log.error("Passenger validator. Passenger already exists by phone. Passenger phone: {}",
                passengerRequestPhone);
            throw new PassengerAlreadyExistsException(
                ExceptionMessageConstant.PASSENGER_ALREADY_EXISTS_BY_PHONE_MESSAGE.formatted(passengerRequestPhone)
            );
        }
    }

}
