package com.vlad.kuzhyr.driverservice.exception;

public class DriverAlreadyExistException extends RuntimeException {
  public DriverAlreadyExistException(String message) {
    super(message);
  }
}
