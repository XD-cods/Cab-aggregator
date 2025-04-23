package com.vlad.kuzhyr.ratingservice.exception;

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

    @ExceptionHandler(value = {
        RatingNotFoundException.class,
        RatingsNotFoundedByPassengerIdException.class,
        RatingsNotFoundedByDriverIdException.class,
        RideInfoPayloadNotFoundException.class
    })
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "404",
            description = "Resource not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ErrorResponse> notFoundException(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder()
            .error(String.valueOf(HttpStatus.NOT_FOUND))
            .errorDescription(exception.getMessage())
            .timestamp(LocalDateTime.now())
            .build());
    }

    @ExceptionHandler(value = {
        RatingAlreadyExistsException.class
    })
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "409",
            description = "Resource already exists",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<ErrorResponse> conflictException(Exception exception) {
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
