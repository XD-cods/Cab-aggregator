package com.vlad.kuzhyr.rideservice.exception;

public class PassengerIsBusyException extends RuntimeException {
    public PassengerIsBusyException(String message) {
        super(message);
    }
}
