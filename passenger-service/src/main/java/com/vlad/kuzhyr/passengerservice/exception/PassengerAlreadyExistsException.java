package com.vlad.kuzhyr.passengerservice.exception;

public class PassengerAlreadyExistsException extends RuntimeException {
  public PassengerAlreadyExistsException(String message) {
    super(message);
  }
}
