package com.vlad.kuzhyr.ratingservice.exception;

public class RatingsNotFoundedByPassengerIdException extends RuntimeException {

    public RatingsNotFoundedByPassengerIdException(String message) {
        super(message);
    }

}