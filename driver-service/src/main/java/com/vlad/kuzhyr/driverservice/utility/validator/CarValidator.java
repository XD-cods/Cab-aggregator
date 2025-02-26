package com.vlad.kuzhyr.driverservice.utility.validator;

import com.vlad.kuzhyr.driverservice.exception.CarAlreadyExistException;
import com.vlad.kuzhyr.driverservice.persistence.repository.CarRepository;
import com.vlad.kuzhyr.driverservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.driverservice.utility.logger.LogUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CarValidator {

    private final CarRepository carRepository;

    public void validateCarByNumber(String carNumber) {
        String maskedCarNumber = LogUtils.maskFormattedCarNumber(carNumber);

        log.debug("validateCarByNumber: Validating car number. Car number: {}",
            maskedCarNumber);

        if (carRepository.existsCarByCarNumberAndIsEnabledTrue(carNumber)) {
            log.error("validateCarByNumber: Car already exists. Car number: {}",
                maskedCarNumber);
            throw new CarAlreadyExistException(
                ExceptionMessageConstant.CAR_ALREADY_EXISTS_BY_CAR_NUMBER_MESSAGE.formatted(carNumber)
            );
        }
    }
}
