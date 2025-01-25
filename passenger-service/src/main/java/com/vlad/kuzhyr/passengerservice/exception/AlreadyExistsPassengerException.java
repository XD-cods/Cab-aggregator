package com.vlad.kuzhyr.passengerservice.exception;

public class AlreadyExistsPassengerException extends RuntimeException {
  public AlreadyExistsPassengerException(String message) {
    super(message);
  }
}
