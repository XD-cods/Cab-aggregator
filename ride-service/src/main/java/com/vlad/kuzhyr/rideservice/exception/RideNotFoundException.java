package com.vlad.kuzhyr.rideservice.exception;

public class RideNotFoundException extends RuntimeException {
  public RideNotFoundException(String message) {
    super(message);
  }

}
