package com.vlad.kuzhyr.ratingservice.web.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RideInfoResponse(

    @Schema(description = "existing ride id", example = "1")
    Long rideId,

    @Schema(description = "existing driver id", example = "1")
    Long driverId,

    @Schema(description = "existing passenger id", example = "1")
    Long passengerId

) {
}