package com.vlad.kuzhyr.passengerservice.utility.validator;

import com.vlad.kuzhyr.passengerservice.exception.PassengerAlreadyExistsException;
import com.vlad.kuzhyr.passengerservice.persistence.repository.PassengerRepository;
import com.vlad.kuzhyr.passengerservice.utility.constant.ExceptionMessageConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PassengerValidator {

    private final PassengerRepository passengerRepository;

    public void validatePassengerEmailAndPhone(String passengerRequestEmail, String passengerRequestPhone) {
        if (passengerRepository.existsPassengerByEmailAndIsEnabledTrue(passengerRequestEmail)) {
            throw new PassengerAlreadyExistsException(
                ExceptionMessageConstant.PASSENGER_ALREADY_EXISTS_BY_EMAIL_MESSAGE.formatted(passengerRequestEmail)
            );
        }

        if (passengerRepository.existsPassengerByPhoneAndIsEnabledTrue(passengerRequestPhone)) {
            throw new PassengerAlreadyExistsException(
                ExceptionMessageConstant.PASSENGER_ALREADY_EXISTS_BY_PHONE_MESSAGE.formatted(passengerRequestPhone)
            );
        }
    }

}
