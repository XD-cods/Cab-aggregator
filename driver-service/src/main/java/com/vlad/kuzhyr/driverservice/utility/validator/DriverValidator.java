package com.vlad.kuzhyr.driverservice.utility.validator;

import com.vlad.kuzhyr.driverservice.exception.DriverAlreadyExistException;
import com.vlad.kuzhyr.driverservice.persistence.repository.DriverRepository;
import com.vlad.kuzhyr.driverservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.driverservice.utility.logger.LogUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DriverValidator {

    private final DriverRepository driverRepository;

    public void validateDriver(String driverRequestEmail, String driverRequestPhone) {

        validateDriverByEmail(driverRequestEmail);
        validateDriverByPhone(driverRequestPhone);

    }

    private void validateDriverByEmail(String driverRequestEmail) {
        String maskedEmail = LogUtils.maskEmail(driverRequestEmail);
        log.debug("validateDriver: Validating driver email. Email: {}", maskedEmail);

        if (driverRepository.existsDriverByEmailAndIsEnabledTrue(driverRequestEmail)) {
            log.error("validateDriver: Email already exists. Email: {}", maskedEmail);
            throw new DriverAlreadyExistException(
                ExceptionMessageConstant.DRIVER_ALREADY_EXISTS_BY_EMAIL_MESSAGE.formatted(driverRequestEmail)
            );
        }
    }

    private void validateDriverByPhone(String driverRequestPhone) {
        String maskedPhone = LogUtils.maskPhone(driverRequestPhone);
        log.debug("validateDriver: Validating driver phone. Phone: {}", maskedPhone);

        if (driverRepository.existsDriverByPhoneAndIsEnabledTrue(driverRequestPhone)) {
            log.error("validateDriver: Phone already exists. Phone: {}", maskedPhone);
            throw new DriverAlreadyExistException(
                ExceptionMessageConstant.DRIVER_ALREADY_EXISTS_BY_PHONE_MESSAGE.formatted(driverRequestPhone)
            );
        }
    }

}
