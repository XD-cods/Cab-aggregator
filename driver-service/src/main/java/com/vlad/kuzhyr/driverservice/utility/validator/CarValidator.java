package com.vlad.kuzhyr.driverservice.utility.validator;

import com.vlad.kuzhyr.driverservice.exception.CarAlreadyExistException;
import com.vlad.kuzhyr.driverservice.persistence.repository.CarRepository;
import com.vlad.kuzhyr.driverservice.utility.constant.ExceptionMessageConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CarValidator {

    private final CarRepository carRepository;

    public void validateCarByNumber(String carNumber) {
        log.debug("Car validation. Start validating car number. Car number: {}", carNumber);

        if (carRepository.existsCarByCarNumberAndIsEnabledTrue(carNumber)) {
            log.error("Car validation. Car already exists by car number. Car number: {}", carNumber);
            throw new CarAlreadyExistException(
                ExceptionMessageConstant.CAR_ALREADY_EXISTS_BY_CAR_NUMBER_MESSAGE.formatted(carNumber)
            );
        }
    }

}
