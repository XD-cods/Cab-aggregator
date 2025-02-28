package com.vlad.kuzhyr.driverservice.utility.validator.unittest;

import com.vlad.kuzhyr.driverservice.constant.UnitTestDataProvider;
import com.vlad.kuzhyr.driverservice.exception.DriverAlreadyExistException;
import com.vlad.kuzhyr.driverservice.persistence.repository.DriverRepository;
import com.vlad.kuzhyr.driverservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.driverservice.utility.validator.DriverValidator;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DriverValidatorTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverValidator driverValidator;

    @Test
    public void validateDriver_shouldThrowDriverAlreadyExistExceptionByEmail() {
        String driverEmail = UnitTestDataProvider.TEST_DRIVER_EMAIL;

        when(driverRepository.existsDriverByEmailAndIsEnabledTrue(driverEmail)).thenReturn(true);

        DriverAlreadyExistException exception = assertThrows(DriverAlreadyExistException.class,
            () -> driverValidator.validateDriver(driverEmail, anyString()));

        assertEquals(exception.getMessage(),
            ExceptionMessageConstant.DRIVER_ALREADY_EXISTS_BY_EMAIL_MESSAGE.formatted(driverEmail));

        verify(driverRepository).existsDriverByEmailAndIsEnabledTrue(driverEmail);
        verify(driverRepository, never()).existsDriverByPhoneAndIsEnabledTrue(any());
    }

    @Test
    public void validateDriver_shouldThrowDriverAlreadyExistExceptionByPhone() {
        String driverEmail = UnitTestDataProvider.TEST_DRIVER_EMAIL;
        String driverPhone = UnitTestDataProvider.TEST_DRIVER_PHONE;

        when(driverRepository.existsDriverByEmailAndIsEnabledTrue(driverEmail)).thenReturn(false);
        when(driverRepository.existsDriverByPhoneAndIsEnabledTrue(driverPhone)).thenReturn(true);

        DriverAlreadyExistException exception = assertThrows(DriverAlreadyExistException.class,
            () -> driverValidator.validateDriver(driverEmail, driverPhone));

        assertEquals(exception.getMessage(),
            ExceptionMessageConstant.DRIVER_ALREADY_EXISTS_BY_PHONE_MESSAGE.formatted(driverPhone));

        verify(driverRepository).existsDriverByEmailAndIsEnabledTrue(driverEmail);
        verify(driverRepository).existsDriverByPhoneAndIsEnabledTrue(driverPhone);

    }

    @Test
    public void validateDriver_shouldNotThrowException() {
        String driverEmail = UnitTestDataProvider.TEST_DRIVER_EMAIL;
        String driverPhone = UnitTestDataProvider.TEST_DRIVER_PHONE;

        when(driverRepository.existsDriverByEmailAndIsEnabledTrue(driverEmail)).thenReturn(false);
        when(driverRepository.existsDriverByPhoneAndIsEnabledTrue(driverPhone)).thenReturn(false);

        assertDoesNotThrow(() -> driverValidator.validateDriver(driverEmail, driverPhone));

        verify(driverRepository).existsDriverByEmailAndIsEnabledTrue(driverEmail);
        verify(driverRepository).existsDriverByPhoneAndIsEnabledTrue(driverPhone);
    }

}
