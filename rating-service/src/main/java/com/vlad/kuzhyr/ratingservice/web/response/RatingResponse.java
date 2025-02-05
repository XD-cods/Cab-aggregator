package com.vlad.kuzhyr.ratingservice.web.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vlad.kuzhyr.ratingservice.persistence.entity.RatedBy;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RatingResponse(

    @Schema(description = "rating id", example = "1")
    Long id,

    @Schema(description = "existing driver id", example = "1")
    Long driverId,

    @Schema(description = "existing ride id", example = "1")
    Long rideId,

    @Schema(description = "existing passenger id", example = "1")
    Long passengerId,

    @Schema(description = "rating", example = "4.5")
    Double rating,

    @Schema(description = "comment of ride", example = "This ride was great!")
    String comment,

    @Schema(description = "who rate", example = "PASSENGER")
    RatedBy ratedBy

) {

}