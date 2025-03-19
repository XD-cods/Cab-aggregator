package com.vlad.kuzhyr.rideservice.exception;

public class PassengerServiceUnavailableException extends RuntimeException {
    public PassengerServiceUnavailableException(String message) {
        super(message);
    }
}
