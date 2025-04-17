package com.vlad.kuzhyr.authservice.exception;

public class UserAlreadyHasRole extends RuntimeException {
    public UserAlreadyHasRole(String message) {
        super(message);
    }
}
