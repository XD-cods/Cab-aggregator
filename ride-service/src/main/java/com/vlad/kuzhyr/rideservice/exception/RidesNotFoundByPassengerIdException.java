package com.vlad.kuzhyr.rideservice.exception;

public class RidesNotFoundByPassengerIdException extends RuntimeException {
  public RidesNotFoundByPassengerIdException(String message) {
    super(message);
  }

}
