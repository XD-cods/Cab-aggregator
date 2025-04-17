package com.vlad.kuzhyr.authservice.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "404",
            description = "User not found by email",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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

    @ExceptionHandler(KeycloakOperationException.class)
    public ResponseEntity<ErrorResponse> keycloakOperationException(KeycloakOperationException e) {
        return ResponseEntity.status(HttpStatus.valueOf(e.getStatusCode())).body(
            ErrorResponse.builder()
                .error(String.valueOf(HttpStatus.valueOf(e.getStatusCode())))
                .errorDescription(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build()
        );
    }

    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "409",
            description = "User already has a role",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @ExceptionHandler(UserAlreadyHasRole.class)
    public ResponseEntity<ErrorResponse> userAlreadyHasRole(UserAlreadyHasRole e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            ErrorResponse.builder()
                .error(String.valueOf(HttpStatus.valueOf(HttpStatus.CONFLICT.value())))
                .errorDescription(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build()
        );
    }

    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "400",
            description = "MethodArgument not valid exception",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
            .map(error -> {
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    return String.format("%s: %s", fieldError.getField(), error.getDefaultMessage());
                }
                return error.getDefaultMessage();
            })
            .collect(Collectors.joining("; "));

        return ResponseEntity.badRequest().body(
            ErrorResponse.builder()
                .error(String.valueOf(HttpStatus.BAD_REQUEST))
                .errorDescription(errorMessage)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }

    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "400",
            description = "Constraint violation exception",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> constraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
            .map(violation -> String.format("%s: %s",
                violation.getPropertyPath().toString(),
                violation.getMessage()))
            .collect(Collectors.joining("; "));

        return ResponseEntity.badRequest().body(
            ErrorResponse.builder()
                .error(String.valueOf(HttpStatus.BAD_REQUEST))
                .errorDescription(errorMessage)
                .timestamp(LocalDateTime.now())
                .build()
        );
    }



}
