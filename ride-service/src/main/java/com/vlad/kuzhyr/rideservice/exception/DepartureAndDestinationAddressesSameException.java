package com.vlad.kuzhyr.rideservice.exception;

public class DepartureAndDestinationAddressesSameException extends RuntimeException {

    public DepartureAndDestinationAddressesSameException(String message) {
        super(message);
    }

}
