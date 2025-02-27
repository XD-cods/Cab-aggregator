package com.vlad.kuzhyr.passengerservice.unittest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.vlad.kuzhyr.passengerservice.exception.PassengerAlreadyExistsException;
import com.vlad.kuzhyr.passengerservice.exception.PassengerNotFoundException;
import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import com.vlad.kuzhyr.passengerservice.persistence.repository.PassengerRepository;
import com.vlad.kuzhyr.passengerservice.service.impl.PassengerServiceImpl;
import com.vlad.kuzhyr.passengerservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.passengerservice.utility.mapper.PassengerMapper;
import com.vlad.kuzhyr.passengerservice.web.dto.request.PassengerRequest;
import com.vlad.kuzhyr.passengerservice.web.dto.response.PassengerResponse;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PassengerServiceImplTest {

    @Mock
    private PassengerRepository passengerRepository;

    @Spy
    private PassengerMapper passengerMapper;

    @InjectMocks
    private PassengerServiceImpl passengerServiceImpl;

    private Passenger passenger;
    private PassengerRequest passengerRequest;
    private PassengerResponse passengerResponse;

    @BeforeEach
    void setUp() {
        passenger = TestDataProvider.createPassenger();
        passengerRequest = TestDataProvider.createPassengerRequest();
        passengerResponse = TestDataProvider.createPassengerResponse();
    }

    @Test
    void getPassengerById_shouldReturnPassengerResponse() {
        Long existingPassengerId = passenger.getId();

        when(passengerRepository.findPassengerByIdAndIsEnabledTrue(existingPassengerId))
            .thenReturn(Optional.of(passenger));
        when(passengerMapper.toResponse(passenger)).thenReturn(passengerResponse);

        PassengerResponse result = passengerServiceImpl.getPassengerById(existingPassengerId);

        assertNotNull(result);
        assertEquals(passengerResponse, result);

        verify(passengerRepository)
            .findPassengerByIdAndIsEnabledTrue(existingPassengerId);
        verify(passengerMapper).toResponse(passenger);
    }

    @Test
    void getPassengerById_shouldThrowNotFoundException() {
        Long nonExistingPassengerId = 2L;

        when(passengerRepository.findPassengerByIdAndIsEnabledTrue(nonExistingPassengerId))
            .thenReturn(Optional.empty());

        PassengerNotFoundException exception = assertThrows(
            PassengerNotFoundException.class,
            () -> passengerServiceImpl.getPassengerById(nonExistingPassengerId)
        );

        assertEquals(
            ExceptionMessageConstant.PASSENGER_NOT_FOUND_MESSAGE.formatted(nonExistingPassengerId),
            exception.getMessage()
        );

        verify(passengerRepository)
            .findPassengerByIdAndIsEnabledTrue(nonExistingPassengerId);
        verifyNoInteractions(passengerMapper);
    }

    @Test
    void createPassenger_shouldReturnPassengerResponse() {
        String passengerRequestEmail = passengerRequest.email();
        String passengerRequestPhone = passengerRequest.phone();

        when(passengerRepository.existsPassengerByEmailAndIsEnabledTrue(passengerRequestEmail))
            .thenReturn(false);
        when(passengerRepository.existsPassengerByPhoneAndIsEnabledTrue(passengerRequestPhone))
            .thenReturn(false);
        when(passengerMapper.toEntity(passengerRequest)).thenReturn(passenger);
        when(passengerRepository.save(passenger)).thenReturn(passenger);
        when(passengerMapper.toResponse(passenger)).thenReturn(passengerResponse);

        PassengerResponse result = passengerServiceImpl.createPassenger(passengerRequest);

        assertNotNull(result);
        assertEquals(passengerResponse, result);

        verify(passengerRepository)
            .existsPassengerByEmailAndIsEnabledTrue(passengerRequestEmail);
        verify(passengerRepository)
            .existsPassengerByPhoneAndIsEnabledTrue(passengerRequestPhone);
        verify(passengerMapper).toEntity(passengerRequest);
        verify(passengerRepository).save(passenger);
        verify(passengerMapper).toResponse(passenger);
    }

    @Test
    void createPassenger_shouldThrowConflictExceptionByEmail() {
        String passengerRequestEmail = passengerRequest.email();

        when(passengerRepository.existsPassengerByEmailAndIsEnabledTrue(passengerRequestEmail))
            .thenReturn(true);

        PassengerAlreadyExistsException exception = assertThrows(
            PassengerAlreadyExistsException.class,
            () -> passengerServiceImpl.createPassenger(passengerRequest)
        );

        assertEquals(
            ExceptionMessageConstant.PASSENGER_ALREADY_EXISTS_BY_EMAIL_MESSAGE.formatted(passengerRequestEmail),
            exception.getMessage()
        );

        verify(passengerRepository)
            .existsPassengerByEmailAndIsEnabledTrue(passengerRequestEmail);
        verifyNoInteractions(passengerMapper);
        verify(passengerRepository, times(0)).save(passenger);
    }

    @Test
    void createPassenger_shouldThrowConflictExceptionByPhone() {
        String passengerRequestPhone = passengerRequest.phone();

        when(passengerRepository.existsPassengerByPhoneAndIsEnabledTrue(passengerRequestPhone))
            .thenReturn(true);

        PassengerAlreadyExistsException exception = assertThrows(
            PassengerAlreadyExistsException.class,
            () -> passengerServiceImpl.createPassenger(passengerRequest)
        );

        assertEquals(
            ExceptionMessageConstant.PASSENGER_ALREADY_EXISTS_BY_PHONE_MESSAGE.formatted(passengerRequestPhone),
            exception.getMessage()
        );

        verify(passengerRepository)
            .existsPassengerByEmailAndIsEnabledTrue(passengerRequest.email());
        verify(passengerRepository)
            .existsPassengerByPhoneAndIsEnabledTrue(passengerRequestPhone);
        verifyNoInteractions(passengerMapper);
        verify(passengerRepository, times(0)).save(passenger);
    }

    @Test
    void updatePassenger_shouldReturnPassengerResponse() {
        Long existingPassengerId = passenger.getId();

        when(passengerRepository.findPassengerByIdAndIsEnabledTrue(existingPassengerId))
            .thenReturn(Optional.of(passenger));
        when(passengerMapper.toResponse(passenger)).thenReturn(passengerResponse);

        PassengerResponse result = passengerServiceImpl.updatePassenger(existingPassengerId, passengerRequest);

        assertNotNull(result);
        assertEquals(passengerResponse, result);

        verify(passengerRepository)
            .findPassengerByIdAndIsEnabledTrue(existingPassengerId);
        verify(passengerMapper).updateFromRequest(passengerRequest, passenger);
        verify(passengerRepository).save(passenger);
        verify(passengerMapper).toResponse(passenger);
    }

    @Test
    void updatePassenger_shouldThrowNotFoundException() {
        Long nonExistingPassengerId = 1L;

        when(passengerRepository.findPassengerByIdAndIsEnabledTrue(nonExistingPassengerId))
            .thenReturn(Optional.empty());

        PassengerNotFoundException exception = assertThrows(
            PassengerNotFoundException.class,
            () -> passengerServiceImpl.updatePassenger(nonExistingPassengerId, passengerRequest)
        );

        assertEquals(
            ExceptionMessageConstant.PASSENGER_NOT_FOUND_MESSAGE.formatted(nonExistingPassengerId),
            exception.getMessage()
        );

        verify(passengerRepository)
            .findPassengerByIdAndIsEnabledTrue(nonExistingPassengerId);
        verifyNoInteractions(passengerMapper);
        verify(passengerRepository, times(0)).save(passenger);
    }

    @Test
    void deletePassengerById_shouldReturnTrue() {
        Long existingPassengerId = passenger.getId();

        when(passengerRepository.findPassengerByIdAndIsEnabledTrue(existingPassengerId))
            .thenReturn(Optional.of(passenger));

        Boolean result = passengerServiceImpl.deletePassengerById(existingPassengerId);

        assertTrue(result);

        verify(passengerRepository)
            .findPassengerByIdAndIsEnabledTrue(existingPassengerId);
        verify(passengerRepository).save(passenger);
    }

    @Test
    void deletePassengerById_shouldThrowNotFoundException() {
        Long nonExistingPassengerId = 2L;

        when(passengerRepository.findPassengerByIdAndIsEnabledTrue(nonExistingPassengerId))
            .thenReturn(Optional.empty());

        PassengerNotFoundException exception = assertThrows(
            PassengerNotFoundException.class,
            () -> passengerServiceImpl.deletePassengerById(nonExistingPassengerId)
        );

        assertEquals(
            ExceptionMessageConstant.PASSENGER_NOT_FOUND_MESSAGE.formatted(nonExistingPassengerId),
            exception.getMessage()
        );

        verify(passengerRepository)
            .findPassengerByIdAndIsEnabledTrue(nonExistingPassengerId);
        verifyNoInteractions(passengerMapper);
        verify(passengerRepository, times(0)).save(passenger);
    }
}
