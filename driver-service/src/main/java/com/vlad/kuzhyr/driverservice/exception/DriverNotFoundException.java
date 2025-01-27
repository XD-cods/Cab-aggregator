package com.vlad.kuzhyr.driverservice.exception;

public class DriverNotFoundException extends RuntimeException {
  public DriverNotFoundException(String message) {
    super(message);
  }
}
