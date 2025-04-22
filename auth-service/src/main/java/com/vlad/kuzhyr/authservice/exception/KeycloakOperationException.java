package com.vlad.kuzhyr.authservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class KeycloakOperationException extends RuntimeException {

    private final HttpStatus statusCode;

    public KeycloakOperationException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
