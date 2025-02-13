package com.vlad.kuzhyr.ratingservice.exception;

public class RatingAlreadyExistsException extends RuntimeException {

    public RatingAlreadyExistsException(String message) {
        super(message);
    }

}