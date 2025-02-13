package com.vlad.kuzhyr.ratingservice.exception;

public class RatingNotFoundException extends RuntimeException {

    public RatingNotFoundException(String message) {
        super(message);
    }

}