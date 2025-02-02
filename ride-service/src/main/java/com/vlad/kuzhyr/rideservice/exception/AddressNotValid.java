package com.vlad.kuzhyr.rideservice.exception;

public class AddressNotValid extends RuntimeException {
  public AddressNotValid(String message) {
    super(message);
  }
}
