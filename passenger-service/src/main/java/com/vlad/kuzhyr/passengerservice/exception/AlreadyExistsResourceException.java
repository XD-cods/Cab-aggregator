package com.vlad.kuzhyr.passengerservice.exception;

public class AlreadyExistsResourceException extends RuntimeException {
  public AlreadyExistsResourceException(String message) {
    super(message);
  }
}
