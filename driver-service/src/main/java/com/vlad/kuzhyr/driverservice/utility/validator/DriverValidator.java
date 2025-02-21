package com.vlad.kuzhyr.driverservice.utility.validator;

import com.vlad.kuzhyr.driverservice.exception.DriverAlreadyExistException;
import com.vlad.kuzhyr.driverservice.persistence.repository.DriverRepository;
import com.vlad.kuzhyr.driverservice.utility.constant.ExceptionMessageConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DriverValidator {

    private final DriverRepository driverRepository;

    public void validateDriver(String driverRequestEmail, String driverRequestPhone) {
        log.debug("Car validation. Start validating driver email. Driver email: {}", driverRequestEmail);

        if (driverRepository.existsDriverByEmailAndIsEnabledTrue(driverRequestEmail)) {
            log.error("Driver validation. Email already exists. Email: {}", driverRequestEmail);
            throw new DriverAlreadyExistException(
                ExceptionMessageConstant.DRIVER_ALREADY_EXISTS_BY_EMAIL_MESSAGE.formatted(driverRequestEmail)
            );
        }

        log.debug("Car validation. Start validating driver phone. Driver phone: {}", driverRequestPhone);

        if (driverRepository.existsDriverByPhoneAndIsEnabledTrue(driverRequestPhone)) {
            log.error("Driver validation. Phone already exists. Phone: {}", driverRequestPhone);
            throw new DriverAlreadyExistException(
                ExceptionMessageConstant.DRIVER_ALREADY_EXISTS_BY_PHONE_MESSAGE.formatted(driverRequestPhone)
            );
        }
    }

}
