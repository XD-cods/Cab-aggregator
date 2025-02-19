package com.vlad.kuzhyr.driverservice.utility.validator;

import com.vlad.kuzhyr.driverservice.exception.DriverAlreadyExistException;
import com.vlad.kuzhyr.driverservice.persistence.repository.DriverRepository;
import com.vlad.kuzhyr.driverservice.utility.constant.ExceptionMessageConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DriverValidator {

    private final DriverRepository driverRepository;

    public void validateDriver(String driverRequestEmail, String driverRequestPhone) {
        if (driverRepository.existsDriverByEmailAndIsEnabledTrue(driverRequestEmail)) {
            throw new DriverAlreadyExistException(
                ExceptionMessageConstant.DRIVER_ALREADY_EXISTS_BY_EMAIL_MESSAGE.formatted(driverRequestEmail)
            );
        }

        if (driverRepository.existsDriverByPhoneAndIsEnabledTrue(driverRequestPhone)) {
            throw new DriverAlreadyExistException(
                ExceptionMessageConstant.DRIVER_ALREADY_EXISTS_BY_PHONE_MESSAGE.formatted(driverRequestPhone)
            );
        }
    }

}
