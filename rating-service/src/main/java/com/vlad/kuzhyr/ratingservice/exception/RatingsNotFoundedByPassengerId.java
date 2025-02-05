package com.vlad.kuzhyr.ratingservice.exception;

public class RatingsNotFoundedByPassengerId extends RuntimeException {

    public RatingsNotFoundedByPassengerId(String message) {
        super(message);
    }

}
