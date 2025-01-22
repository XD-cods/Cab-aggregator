package com.vlad.kuzhyr.passengerservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> notFoundException(NotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder()
            .error(String.valueOf(HttpStatus.NOT_FOUND))
            .errorDescription(exception.getMessage())
            .build());
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorResponse> notFoundException(ConflictException exception) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.builder()
            .error(String.valueOf(HttpStatus.CONFLICT))
            .errorDescription(exception.getMessage())
            .build());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> badRequestException(BadRequestException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder()
            .error(String.valueOf(HttpStatus.BAD_REQUEST))
            .errorDescription(exception.getMessage())
            .build());
  }
}
