package com.vlad.kuzhyr.rideservice.exception;

public class NotValidStatusTransitionException extends RuntimeException {

    public NotValidStatusTransitionException(String message) {
        super(message);
    }

}
