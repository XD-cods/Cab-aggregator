package com.vlad.kuzhyr.authservice.exception;

public class PassengerServiceUnavailableException extends RuntimeException {
    public PassengerServiceUnavailableException(String message) {
        super(message);
    }
}
