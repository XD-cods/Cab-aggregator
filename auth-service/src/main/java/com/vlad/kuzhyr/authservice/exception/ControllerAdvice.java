package com.vlad.kuzhyr.authservice.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(KeycloakOperationException.class)
    public ResponseEntity<ErrorResponse> userCreateException(KeycloakOperationException e) {
        return ResponseEntity.status(e.getStatusCode()).body(
            ErrorResponse.builder()
                .error(String.valueOf(e.getStatusCode()))
                .errorDescription(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build()
        );
    }

    @ExceptionHandler(KeycloakUserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userCreateException(KeycloakUserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse.builder()
                .error(String.valueOf(HttpStatus.NOT_FOUND))
                .errorDescription(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> userCreateException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse.builder()
                .error(String.valueOf(HttpStatus.BAD_REQUEST))
                .errorDescription(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build()
        );
    }

}
