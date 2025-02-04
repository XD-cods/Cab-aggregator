package com.vlad.kuzhyr.rideservice.exception;

public class RidesNotFoundByDriverIdException extends RuntimeException {

    public RidesNotFoundByDriverIdException(String message) {
        super(message);
    }

}
