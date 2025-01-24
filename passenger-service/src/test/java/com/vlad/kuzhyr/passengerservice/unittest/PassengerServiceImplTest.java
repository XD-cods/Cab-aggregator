package com.vlad.kuzhyr.passengerservice.unittest;

import com.vlad.kuzhyr.passengerservice.exception.ConflictException;
import com.vlad.kuzhyr.passengerservice.exception.NotFoundException;
import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import com.vlad.kuzhyr.passengerservice.persistence.repository.PassengerRepository;
import com.vlad.kuzhyr.passengerservice.service.impl.PassengerServiceImpl;
import com.vlad.kuzhyr.passengerservice.utility.constant.PassengerServiceConstant;
import com.vlad.kuzhyr.passengerservice.utility.mapper.PassengerMapper;
import com.vlad.kuzhyr.passengerservice.web.request.PassengerRequest;
import com.vlad.kuzhyr.passengerservice.web.response.PassengerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


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
    passenger = Passenger.builder()
            .id(1L)
            .phone("1234567890")
            .email("test@example.com")
            .firstName("John")
            .lastName("Doe")
            .isEnabled(true)
            .build();

    passengerRequest = PassengerRequest.builder()
            .phone("1234567890")
            .email("test@example.com")
            .firstName("John")
            .lastName("Doe")
            .build();

    passengerResponse = PassengerResponse.builder()
            .id(1L)
            .phone("1234567890")
            .email("test@example.com")
            .firstName("John")
            .lastName("Doe")
            .build();
  }

  @Test
  void getPassengerById_shouldReturnPassengerResponse() {
    when(passengerRepository.findPassengerByIdAndIsEnabledTrue(1L)).thenReturn(Optional.of(passenger));
    when(passengerMapper.toResponse(passenger)).thenReturn(passengerResponse);

    PassengerResponse result = passengerServiceImpl.getPassengerById(1L);

    assertNotNull(result);
    assertEquals(passengerResponse, result);
  }

  @Test
  void getPassengerById_shouldThrowNotFoundException() {
    when(passengerRepository.findPassengerByIdAndIsEnabledTrue(1L)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class, () ->
            passengerServiceImpl.getPassengerById(1L));

    assertEquals(String.format(PassengerServiceConstant.NOT_FOUND_MESSAGE, 1L), exception.getMessage());
  }

  @Test
  void createPassenger_shouldReturnPassengerResponse() {
    when(passengerRepository.existsPassengersByEmailOrPhone("test@example.com", "1234567890")).thenReturn(false);
    when(passengerMapper.toEntity(passengerRequest)).thenReturn(passenger);
    when(passengerRepository.save(passenger)).thenReturn(passenger);
    when(passengerMapper.toResponse(passenger)).thenReturn(passengerResponse);

    PassengerResponse result = passengerServiceImpl.createPassenger(passengerRequest);

    assertNotNull(result);
    assertEquals(passengerResponse, result);
  }

  @Test
  void createPassenger_shouldThrowConflictException() {
    when(passengerRepository.existsPassengersByEmailOrPhone("test@example.com", "1234567890")).thenReturn(true);

    ConflictException exception = assertThrows(ConflictException.class, () ->
            passengerServiceImpl.createPassenger(passengerRequest));

    assertEquals(String.format(PassengerServiceConstant.CONFLICT_BY_EMAIL_OR_PHONE, "test@example.com", "1234567890"), exception.getMessage());
  }

  @Test
  void updatePassenger_shouldReturnPassengerResponse() {
    when(passengerRepository.findPassengerByIdAndIsEnabledTrue(1L)).thenReturn(Optional.of(passenger));
    when(passengerMapper.toResponse(passenger)).thenReturn(passengerResponse);

    PassengerResponse result = passengerServiceImpl.updatePassenger(1L, passengerRequest);

    assertNotNull(result);
    assertEquals(passengerResponse, result);
  }

  @Test
  void updatePassenger_shouldThrowNotFoundException() {
    when(passengerRepository.findPassengerByIdAndIsEnabledTrue(1L)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class, () ->
            passengerServiceImpl.updatePassenger(1L, passengerRequest));

    assertEquals(String.format(PassengerServiceConstant.NOT_FOUND_MESSAGE, 1L), exception.getMessage());
  }

  @Test
  void deletePassengerById_shouldReturnTrue() {
    when(passengerRepository.findPassengerByIdAndIsEnabledTrue(1L)).thenReturn(Optional.of(passenger));

    Boolean result = passengerServiceImpl.deletePassengerById(1L);

    assertTrue(result);
  }

  @Test
  void deletePassengerById_shouldThrowNotFoundException() {
    when(passengerRepository.findPassengerByIdAndIsEnabledTrue(1L)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class, () ->
            passengerServiceImpl.deletePassengerById(1L));

    assertEquals(String.format(PassengerServiceConstant.NOT_FOUND_MESSAGE, 1L), exception.getMessage());
  }
}