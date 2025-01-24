package com.vlad.kuzhyr.driverservice.exception;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ErrorResponse {

  @Schema(description = "Describes error http status code and error name", example = "404 NOT_FOUND")
  private String error;

  @Schema(description = "Describes the error in more detail", example = "Passenger not found by id: 1")
  private String errorDescription;

  @Schema(description = "Describes when the error occurred ", example = "2014-04-08 12:30")
  private LocalDateTime timestamp;
}
