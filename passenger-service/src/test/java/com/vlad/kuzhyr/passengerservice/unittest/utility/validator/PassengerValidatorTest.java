package com.vlad.kuzhyr.passengerservice.unittest.utility.validator;

import com.vlad.kuzhyr.passengerservice.constant.TestDataProvider;
import com.vlad.kuzhyr.passengerservice.exception.PassengerAlreadyExistsException;
import com.vlad.kuzhyr.passengerservice.persistence.repository.PassengerRepository;
import com.vlad.kuzhyr.passengerservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.passengerservice.utility.validator.PassengerValidator;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PassengerValidatorTest {

    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private PassengerValidator passengerValidator;

    private final String passengerRequestEmail = TestDataProvider.TEST_EMAIL;
    private final String passengerRequestPhone = TestDataProvider.TEST_PHONE;

    @Test
    void validatePassengerEmailAndPhone_shouldThrowConflictExceptionByEmail() {

        when(passengerRepository.existsPassengerByEmailAndIsEnabledTrue(passengerRequestEmail))
            .thenReturn(true);

        PassengerAlreadyExistsException exception = assertThrows(
            PassengerAlreadyExistsException.class,
            () -> passengerValidator.validatePassengerEmailAndPhone(passengerRequestEmail, passengerRequestPhone)
        );

        assertEquals(
            ExceptionMessageConstant.PASSENGER_ALREADY_EXISTS_BY_EMAIL_MESSAGE.formatted(passengerRequestEmail),
            exception.getMessage()
        );

        verify(passengerRepository).existsPassengerByEmailAndIsEnabledTrue(passengerRequestEmail);
        verify(passengerRepository, never())
            .existsPassengerByPhoneAndIsEnabledTrue(any());
    }

    @Test
    void validatePassengerEmailAndPhone_shouldThrowConflictExceptionByPhone() {

        when(passengerRepository.existsPassengerByEmailAndIsEnabledTrue(passengerRequestEmail))
            .thenReturn(false);
        when(passengerRepository.existsPassengerByPhoneAndIsEnabledTrue(passengerRequestPhone))
            .thenReturn(true);

        PassengerAlreadyExistsException exception = assertThrows(
            PassengerAlreadyExistsException.class,
            () -> passengerValidator.validatePassengerEmailAndPhone(passengerRequestEmail, passengerRequestPhone)
        );

        assertEquals(
            ExceptionMessageConstant.PASSENGER_ALREADY_EXISTS_BY_PHONE_MESSAGE.formatted(passengerRequestPhone),
            exception.getMessage()
        );

        verify(passengerRepository).existsPassengerByEmailAndIsEnabledTrue(passengerRequestEmail);
        verify(passengerRepository).existsPassengerByPhoneAndIsEnabledTrue(passengerRequestPhone);
    }

    @Test
    void validatePassengerEmailAndPhone_shouldNotThrowException() {

        when(passengerRepository.existsPassengerByEmailAndIsEnabledTrue(passengerRequestEmail))
            .thenReturn(false);
        when(passengerRepository.existsPassengerByPhoneAndIsEnabledTrue(passengerRequestPhone))
            .thenReturn(false);

        assertDoesNotThrow(
            () -> passengerValidator.validatePassengerEmailAndPhone(passengerRequestEmail, passengerRequestPhone));

        verify(passengerRepository).existsPassengerByEmailAndIsEnabledTrue(passengerRequestEmail);
        verify(passengerRepository).existsPassengerByPhoneAndIsEnabledTrue(passengerRequestPhone);
    }

}
