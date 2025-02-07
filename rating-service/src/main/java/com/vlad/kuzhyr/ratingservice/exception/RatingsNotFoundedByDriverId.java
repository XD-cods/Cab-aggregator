package com.vlad.kuzhyr.ratingservice.exception;

public class RatingsNotFoundedByDriverId extends RuntimeException {

    public RatingsNotFoundedByDriverId(String message) {
        super(message);
    }

}