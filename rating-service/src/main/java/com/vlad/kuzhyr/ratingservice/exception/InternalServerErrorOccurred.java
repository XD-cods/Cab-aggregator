package com.vlad.kuzhyr.ratingservice.exception;

public class InternalServerErrorOccurred extends RuntimeException {

    public InternalServerErrorOccurred(String message) {
        super(message);
    }
}