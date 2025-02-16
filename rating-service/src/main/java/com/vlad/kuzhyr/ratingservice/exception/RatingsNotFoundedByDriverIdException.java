package com.vlad.kuzhyr.ratingservice.exception;

public class RatingsNotFoundedByDriverIdException extends RuntimeException {

    public RatingsNotFoundedByDriverIdException(String message) {
        super(message);
    }

}