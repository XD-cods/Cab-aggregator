package com.vlad.kuzhyr.authservice.exception;

public class KeycloakUserNotFoundException extends RuntimeException {
    public KeycloakUserNotFoundException(String message) {
        super(message);
    }
}
