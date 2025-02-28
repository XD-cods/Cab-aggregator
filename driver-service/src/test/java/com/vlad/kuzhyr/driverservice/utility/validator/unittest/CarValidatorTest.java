package com.vlad.kuzhyr.driverservice.utility.validator.unittest;

import com.vlad.kuzhyr.driverservice.constant.UnitTestDataProvider;
import com.vlad.kuzhyr.driverservice.exception.CarAlreadyExistException;
import com.vlad.kuzhyr.driverservice.persistence.repository.CarRepository;
import com.vlad.kuzhyr.driverservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.driverservice.utility.validator.CarValidator;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CarValidatorTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarValidator carValidator;

    @Test
    public void validateCarByNumber_shouldThrowCarAlreadyExistsException() {
        String carNumber = UnitTestDataProvider.TEST_CAR_NUMBER;

        when(carRepository.existsCarByCarNumberAndIsEnabledTrue(carNumber)).thenReturn(true);

        CarAlreadyExistException exception =
            assertThrows(CarAlreadyExistException.class, () -> carValidator.validateCarByNumber(carNumber));

        assertEquals(exception.getMessage(),
            ExceptionMessageConstant.CAR_ALREADY_EXISTS_BY_CAR_NUMBER_MESSAGE.formatted(carNumber));

        verify(carRepository).existsCarByCarNumberAndIsEnabledTrue(carNumber);
    }

    @Test
    public void validateCarByNumber_shouldNotThrowCarAlreadyExistsException() {
        String carNumber = UnitTestDataProvider.TEST_CAR_NUMBER;

        when(carRepository.existsCarByCarNumberAndIsEnabledTrue(carNumber)).thenReturn(false);

        assertDoesNotThrow(() -> carValidator.validateCarByNumber(carNumber));

        verify(carRepository).existsCarByCarNumberAndIsEnabledTrue(carNumber);
    }

}
