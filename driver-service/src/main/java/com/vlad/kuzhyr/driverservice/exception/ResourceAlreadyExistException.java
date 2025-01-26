package com.vlad.kuzhyr.driverservice.exception;

public class ResourceAlreadyExistException extends RuntimeException {
  public ResourceAlreadyExistException(String message) {
    super(message);
  }
}
