package com.vlad.kuzhyr.passengerservice.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(PassengerNotFoundException.class)
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "404",
            description = "Resource not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ErrorResponse> notFoundException(PassengerNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder()
            .error(String.valueOf(HttpStatus.NOT_FOUND))
            .errorDescription(exception.getMessage())
            .timestamp(LocalDateTime.now())
            .build());
    }

    @ExceptionHandler(PassengerAlreadyExistsException.class)
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "409",
            description = "Conflict occurred",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ErrorResponse> conflictException(PassengerAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.builder()
            .error(String.valueOf(HttpStatus.CONFLICT))
            .errorDescription(exception.getMessage())
            .timestamp(LocalDateTime.now())
            .build());
    }

    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @ExceptionHandler(InternalServerErrorOccurred.class)
    public ResponseEntity<ErrorResponse> internalServerErrorOccurred(InternalServerErrorOccurred exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.builder()
            .error(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
            .errorDescription(exception.getMessage())
            .timestamp(LocalDateTime.now())
            .build());
    }

    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "400",
            description = "Request arguments not valid exception",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @ExceptionHandler(value = {
        MethodArgumentNotValidException.class,
    })
    public ResponseEntity<ErrorResponse> requestValidationException(MethodArgumentNotValidException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder()
            .error(String.valueOf(HttpStatus.BAD_REQUEST))
            .errorDescription(exception.getMessage())
            .timestamp(LocalDateTime.now())
            .build());
    }

    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "400",
            description = "Constraint violation exception",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder()
            .error(String.valueOf(HttpStatus.BAD_REQUEST))
            .errorDescription(exception.getMessage())
            .timestamp(LocalDateTime.now())
            .build());
    }
}
