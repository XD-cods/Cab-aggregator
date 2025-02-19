package com.vlad.kuzhyr.driverservice.utility.validator;

import com.vlad.kuzhyr.driverservice.exception.CarAlreadyExistException;
import com.vlad.kuzhyr.driverservice.persistence.repository.CarRepository;
import com.vlad.kuzhyr.driverservice.utility.constant.ExceptionMessageConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarValidator {

    private final CarRepository carRepository;

    public void validateCarByNumber(String carNumber) {
        if (carRepository.existsCarByCarNumberAndIsEnabledTrue(carNumber)) {
            throw new CarAlreadyExistException(
                ExceptionMessageConstant.CAR_ALREADY_EXISTS_BY_CAR_NUMBER_MESSAGE.formatted(carNumber)
            );
        }
    }

}
