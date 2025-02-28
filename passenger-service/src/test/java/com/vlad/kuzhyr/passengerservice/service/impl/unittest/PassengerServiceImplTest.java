package com.vlad.kuzhyr.passengerservice.service.impl.unittest;

import com.vlad.kuzhyr.passengerservice.constant.UnitTestDataProvider;
import com.vlad.kuzhyr.passengerservice.exception.PassengerNotFoundException;
import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import com.vlad.kuzhyr.passengerservice.persistence.repository.PassengerRepository;
import com.vlad.kuzhyr.passengerservice.service.impl.PassengerServiceImpl;
import com.vlad.kuzhyr.passengerservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.passengerservice.utility.mapper.PageResponseMapper;
import com.vlad.kuzhyr.passengerservice.utility.mapper.PassengerMapper;
import com.vlad.kuzhyr.passengerservice.utility.validator.PassengerValidator;
import com.vlad.kuzhyr.passengerservice.web.dto.request.PassengerRequest;
import com.vlad.kuzhyr.passengerservice.web.dto.response.PageResponse;
import com.vlad.kuzhyr.passengerservice.web.dto.response.PassengerResponse;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class PassengerServiceImplTest {

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private PassengerValidator passengerValidator;

    @Mock
    private PageResponseMapper pageResponseMapper;

    @Mock
    private PassengerMapper passengerMapper;

    @InjectMocks
    private PassengerServiceImpl passengerServiceImpl;

    private Passenger passenger;
    private PassengerResponse passengerResponse;

    @BeforeEach
    void setUp() {
        passenger = UnitTestDataProvider.passenger();
        passengerResponse = UnitTestDataProvider.passengerResponse();
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

        verify(passengerRepository).findPassengerByIdAndIsEnabledTrue(existingPassengerId);
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

        verify(passengerRepository).findPassengerByIdAndIsEnabledTrue(nonExistingPassengerId);
        verifyNoInteractions(passengerMapper);
    }

    @ParameterizedTest
    @CsvSource({
        "0, 10",
        "1, 20",
        "2, 5"
    })
    void getPassengers_shouldReturnPageResponse(int currentPage, int limit) {
        PageRequest pageRequest = PageRequest.of(currentPage, limit);

        List<Passenger> passengers = List.of(passenger);
        Page<Passenger> passengerPage = new PageImpl<>(passengers, pageRequest, passengers.size());

        when(passengerRepository.findAll(pageRequest)).thenReturn(passengerPage);
        when(pageResponseMapper.toPageResponse(eq(passengerPage), eq(currentPage), any()))
            .thenReturn(new PageResponse<>(List.of(passengerResponse), currentPage, passengerPage.getTotalElements(),
                passengerPage.getTotalPages()));

        PageResponse<PassengerResponse> result = passengerServiceImpl.getPassengers(currentPage, limit);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals(currentPage, result.currentPage());
        assertEquals(passengerPage.getTotalPages(), result.totalPages());
        assertEquals(passengerPage.getTotalElements(), result.totalElements());

        verify(passengerRepository).findAll(pageRequest);
        verify(pageResponseMapper).toPageResponse(eq(passengerPage), eq(currentPage), any());
    }

    @Test
    void createPassenger_shouldReturnPassengerResponse() {
        PassengerRequest passengerRequest = UnitTestDataProvider.passengerCreateRequest();
        PassengerResponse passengerCreateResponse = UnitTestDataProvider.passengerCreateResponse();
        String passengerRequestEmail = UnitTestDataProvider.TEST_EMAIL;
        String passengerRequestPhone = UnitTestDataProvider.TEST_PHONE;

        when(passengerMapper.toEntity(passengerRequest)).thenReturn(passenger);
        when(passengerRepository.save(passenger)).thenReturn(passenger);
        when(passengerMapper.toResponse(passenger)).thenReturn(passengerCreateResponse);

        PassengerResponse result = passengerServiceImpl.createPassenger(passengerRequest);

        assertNotNull(result);
        assertEquals(passengerCreateResponse, result);

        verify(passengerValidator).validatePassengerEmailAndPhone(passengerRequestEmail, passengerRequestPhone);
        verify(passengerMapper).toEntity(passengerRequest);
        verify(passengerRepository).save(passenger);
        verify(passengerMapper).toResponse(passenger);
    }

    @Test
    void updatePassenger_shouldReturnPassengerResponse() {
        Long existingPassengerId = passenger.getId();
        PassengerRequest updateRequest = UnitTestDataProvider.passengerUpdateRequest();
        PassengerResponse updatedResponse = UnitTestDataProvider.passengerUpdateResponse();

        when(passengerRepository.findPassengerByIdAndIsEnabledTrue(existingPassengerId))
            .thenReturn(Optional.of(passenger));
        when(passengerRepository.save(passenger)).thenReturn(passenger);
        when(passengerMapper.toResponse(passenger)).thenReturn(updatedResponse);

        PassengerResponse result = passengerServiceImpl.updatePassenger(existingPassengerId, updateRequest);

        assertNotNull(result);
        assertEquals(updatedResponse, result);

        verify(passengerRepository).findPassengerByIdAndIsEnabledTrue(existingPassengerId);
        verify(passengerMapper).updateFromRequest(updateRequest, passenger);
        verify(passengerRepository).save(passenger);
        verify(passengerMapper).toResponse(passenger);
    }

    @Test
    void deletePassengerById_shouldReturnTrue() {
        Long existingPassengerId = passenger.getId();

        when(passengerRepository.findPassengerByIdAndIsEnabledTrue(existingPassengerId))
            .thenReturn(Optional.of(passenger));

        Boolean result = passengerServiceImpl.deletePassengerById(existingPassengerId);

        assertTrue(result);
        assertFalse(passenger.getIsEnabled());

        verify(passengerRepository).findPassengerByIdAndIsEnabledTrue(existingPassengerId);
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

        verify(passengerRepository).findPassengerByIdAndIsEnabledTrue(nonExistingPassengerId);
        verifyNoInteractions(passengerMapper);
        verify(passengerRepository, never()).save(passenger);
    }

    @Test
    void getPassengerById_shouldThrowNotFoundExceptionForDisabledPassenger() {
        Passenger disabledPassenger = UnitTestDataProvider.passengerDisabled();
        Long disabledPassengerId = disabledPassenger.getId();

        when(passengerRepository.findPassengerByIdAndIsEnabledTrue(disabledPassengerId))
            .thenReturn(Optional.empty());

        PassengerNotFoundException exception = assertThrows(
            PassengerNotFoundException.class,
            () -> passengerServiceImpl.getPassengerById(disabledPassengerId)
        );

        assertEquals(
            ExceptionMessageConstant.PASSENGER_NOT_FOUND_MESSAGE.formatted(disabledPassengerId),
            exception.getMessage()
        );

        verify(passengerRepository).findPassengerByIdAndIsEnabledTrue(disabledPassengerId);
        verifyNoInteractions(passengerMapper);
    }
}