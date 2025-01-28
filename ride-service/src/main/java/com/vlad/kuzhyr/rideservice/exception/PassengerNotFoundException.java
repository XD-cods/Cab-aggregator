package com.vlad.kuzhyr.rideservice.exception;

public class PassengerNotFoundException extends RuntimeException {
  public PassengerNotFoundException(String message) {
    super(message);
  }

}
