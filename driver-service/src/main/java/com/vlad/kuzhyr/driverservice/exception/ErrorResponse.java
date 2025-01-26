package com.vlad.kuzhyr.driverservice.exception;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ErrorResponse(

        @Schema(description = "Describes error http status code and error name", example = "404 NOT_FOUND")
        String error,

        @Schema(description = "Describes the error in more detail", example = "Passenger not found by id: 1")
        String errorDescription,

        @Schema(description = "Describes when the error occurred ", example = "2014-04-08 12:30")
        LocalDateTime timestamp

) {
}
