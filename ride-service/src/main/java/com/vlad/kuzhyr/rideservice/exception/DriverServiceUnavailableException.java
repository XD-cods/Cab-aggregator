package com.vlad.kuzhyr.rideservice.exception;

public class DriverServiceUnavailableException extends RuntimeException {
    public DriverServiceUnavailableException(String message) {
        super(message);
    }
}
