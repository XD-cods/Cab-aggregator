package com.vlad.kuzhyr.passengerservice.utility.validator;

import com.vlad.kuzhyr.passengerservice.exception.PassengerAlreadyExistsException;
import com.vlad.kuzhyr.passengerservice.persistence.repository.PassengerRepository;
import com.vlad.kuzhyr.passengerservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.passengerservice.utility.logger.LogUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PassengerValidator {

    private final PassengerRepository passengerRepository;

    public void validatePassengerEmailAndPhone(String passengerRequestEmail, String passengerRequestPhone) {
        validatePassengerByEmail(passengerRequestEmail);
        validatePassengerByPhone(passengerRequestPhone);
    }

    private void validatePassengerByPhone(String passengerRequestPhone) {
        String maskedPhone = LogUtils.maskPhone(passengerRequestPhone);

        log.debug("validatePassengerByPhone: Validating passenger phone. Phone: {}", maskedPhone);

        if (passengerRepository.existsPassengerByPhoneAndIsEnabledTrue(passengerRequestPhone)) {
            log.error("validatePassengerByPhone: Passenger already exists by phone. Phone: {}",
                maskedPhone);
            throw new PassengerAlreadyExistsException(
                ExceptionMessageConstant.PASSENGER_ALREADY_EXISTS_BY_PHONE_MESSAGE.formatted(passengerRequestPhone)
            );
        }
    }

    private void validatePassengerByEmail(String passengerRequestEmail) {
        String maskedEmail = LogUtils.maskEmail(passengerRequestEmail);

        log.debug("validatePassengerByEmail: Validating passenger email. Email: {}", maskedEmail);

        if (passengerRepository.existsPassengerByEmailAndIsEnabledTrue(passengerRequestEmail)) {
            log.error("validatePassengerByEmail: Passenger already exists by email. Email: {}",
                maskedEmail);
            throw new PassengerAlreadyExistsException(
                ExceptionMessageConstant.PASSENGER_ALREADY_EXISTS_BY_EMAIL_MESSAGE.formatted(passengerRequestEmail)
            );
        }
    }

}
