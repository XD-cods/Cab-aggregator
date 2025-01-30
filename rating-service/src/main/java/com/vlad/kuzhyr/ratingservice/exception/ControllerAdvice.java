package com.vlad.kuzhyr.ratingservice.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerAdvice {

  @ExceptionHandler(RatingNotFoundException.class)
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

  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "500",
                  description = "Internal server error occurred",
                  content = @Content(schema = @Schema(implementation = ErrorResponse.class)
                  )
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
                  content = @Content(schema = @Schema(implementation = ErrorResponse.class)
                  )
          )
  })
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> requestValidationException(MethodArgumentNotValidException exception) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder()
            .error(String.valueOf(HttpStatus.BAD_REQUEST))
            .errorDescription(exception.getMessage())
            .timestamp(LocalDateTime.now())
            .build());
  }

}
