package com.vlad.kuzhyr.rideservice.exception;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(value = {
        RideNotFoundException.class,
        RidesNotFoundByPassengerIdException.class,
        RidesNotFoundByDriverIdException.class,
    })
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "404",
            description = "Resource not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ErrorResponse> notFoundException(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder()
            .error(String.valueOf(HttpStatus.NOT_FOUND))
            .errorDescription(exception.getMessage())
            .timestamp(LocalDateTime.now())
            .build());
    }

    @ExceptionHandler(value = {FeignException.class})
    public ResponseEntity<Map<String, String>> handleFeignStatusException(FeignException e) {
        try {
            Map<String, String> errorBody = objectMapper.readValue(
                e.contentUTF8(),
                new TypeReference<>() {
                }
            );
            return ResponseEntity
                .status(e.status())
                .body(errorBody);
        } catch (Exception ex) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal Server Error", "message", e.getMessage()));
        }
    }

    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error occurred",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
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
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @ExceptionHandler(value = {
        MethodArgumentNotValidException.class,
        AddressNotValidException.class,
        DistanceExtractionException.class,
        NotValidStatusTransitionException.class,
        RideCanNotUpdatableException.class,
        DepartureAndDestinationAddressesSameException.class,
        DriverIsBusyException.class,
        PassengerIsBusyException.class,
        DriverHasNotCarException.class,
        NewAddressAndCurrentAddressSameException.class
    })
    public ResponseEntity<ErrorResponse> requestValidationException(Exception exception) {
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

    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "503",
            description = "External service is unavailable",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @ExceptionHandler(value = {
        DriverServiceUnavailableException.class,
        PassengerServiceUnavailableException.class
    })
    public ResponseEntity<ErrorResponse> handleDriverServiceUnavailable(DriverServiceUnavailableException exception) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ErrorResponse.builder()
            .error(String.valueOf(HttpStatus.SERVICE_UNAVAILABLE))
            .errorDescription(exception.getMessage())
            .timestamp(LocalDateTime.now())
            .build());
    }

}
