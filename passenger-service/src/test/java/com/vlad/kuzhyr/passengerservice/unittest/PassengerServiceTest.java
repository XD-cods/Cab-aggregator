package com.vlad.kuzhyr.passengerservice.unittest;

import com.vlad.kuzhyr.passengerservice.exception.BadRequestException;
import com.vlad.kuzhyr.passengerservice.exception.ConflictException;
import com.vlad.kuzhyr.passengerservice.exception.NotFoundException;
import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import com.vlad.kuzhyr.passengerservice.persistence.repository.PassengerRepository;
import com.vlad.kuzhyr.passengerservice.services.PassengerService;
import com.vlad.kuzhyr.passengerservice.utility.PassengerMapper;
import com.vlad.kuzhyr.passengerservice.web.request.PassengerRequest;
import com.vlad.kuzhyr.passengerservice.web.response.PassengerResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PassengerServiceTest {

  ModelMapper modelMapper = new ModelMapper();

  @Mock
  private PassengerRepository passengerRepository;
  @Spy
  private PassengerMapper passengerMapper;
  @InjectMocks
  private PassengerService passengerService;

  private Passenger passenger;
  private PassengerRequest passengerRequest;
  private PassengerResponse passengerResponse;

  public PassengerServiceTest() {
    modelMapper.getConfiguration().setSkipNullEnabled(true);
    passengerMapper = new PassengerMapper(modelMapper);
  }

  @BeforeEach
  void setUp() {
    passenger = Passenger.builder()
            .id(1L)
            .phone("1234567890")
            .email("test@example.com")
            .firstName("John")
            .lastName("Doe")
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
    when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

    PassengerResponse result = passengerService.getPassengerById(1L);

    assertNotNull(result);
    assertEquals(passengerResponse, result);
  }

  @Test
  void getPassengerById_shouldThrowNotFoundException() {
    when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class, () ->
            passengerService.getPassengerById(1L));

    assertEquals("passenger not found by id: 1", exception.getMessage());
  }

  @Test
  void getPassengerById_shouldThrowBadRequestException() {
    passenger.setIsEnabled(false);
    when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

    BadRequestException exception = assertThrows(BadRequestException.class, () ->
            passengerService.getPassengerById(1L));

    assertEquals("passenger not available by id: 1", exception.getMessage());
  }

  @Test
  void createPassenger_shouldReturnPassengerResponse() {
    when(passengerRepository.findPassengerByEmail("test@example.com")).thenReturn(Optional.empty());
    when(passengerRepository.save(any(Passenger.class))).thenReturn(passenger);

    PassengerResponse result = passengerService.createPassenger(passengerRequest);

    assertNotNull(result);
    assertEquals(passengerResponse, result);
  }

  @Test
  void createPassenger_shouldThrowConflictException() {
    when(passengerRepository.findPassengerByEmail("test@example.com")).thenReturn(Optional.of(passenger));

    ConflictException exception = assertThrows(ConflictException.class, () ->
            passengerService.createPassenger(passengerRequest));

    assertEquals("passenger exist by email: test@example.com", exception.getMessage());
  }

  @Test
  void updatePassenger_shouldReturnPassengerResponse() {
    when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

    PassengerResponse result = passengerService.updatePassenger(1L, passengerRequest);

    assertNotNull(result);
    assertEquals(passengerResponse, result);
  }

  @Test
  void updatePassenger_shouldThrowNotFoundException() {
    when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class, () ->
            passengerService.updatePassenger(1L, passengerRequest));

    assertEquals("passenger not found by id: 1", exception.getMessage());
  }

  @Test
  void updatePassenger_shouldThrowBadRequestException() {
    passenger.setIsEnabled(false);
    when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

    BadRequestException exception = assertThrows(BadRequestException.class, () ->
            passengerService.updatePassenger(1L, passengerRequest));

    assertEquals("passenger not available by id: 1", exception.getMessage());
  }

  @Test
  void deletePassengerById_shouldReturnTrue() {
    when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

    Boolean result = passengerService.deletePassengerById(1L);

    assertTrue(result);
  }

  @Test
  void deletePassengerById_shouldThrowNotFoundException() {
    when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class, () ->
            passengerService.deletePassengerById(1L));

    assertEquals("passenger not found by id: 1", exception.getMessage());
  }

  @Test
  void deletePassengerById_shouldThrowBadRequestException() {
    passenger.setIsEnabled(false);
    when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));

    BadRequestException exception = assertThrows(BadRequestException.class, () ->
            passengerService.deletePassengerById(1L));

    assertEquals("passenger not available by id: 1", exception.getMessage());
  }
}