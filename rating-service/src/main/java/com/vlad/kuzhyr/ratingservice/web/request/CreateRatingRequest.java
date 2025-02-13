package com.vlad.kuzhyr.ratingservice.web.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vlad.kuzhyr.ratingservice.persistence.entity.RatedBy;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateRatingRequest(

    @NotNull(message = "{validation.rating.null}")
    @Schema(description = "existing ride id", example = "1")
    Long rideId,

    @NotNull(message = "{validation.driver-id.null}")
    @Schema(description = "existing driver id", example = "1")
    Long driverId,

    @NotNull(message = "{validation.passenger-id.null}")
    @Schema(description = "existing passenger id", example = "1")
    Long passengerId,

    @NotNull(message = "{validation.rating.null}")
    @Min(value = 0, message = "{validation.rating.small}")
    @Max(value = 5, message = "{validation.rating.over}")
    @Schema(description = "rating", example = "4.5")
    Double rating,

    @Schema(description = "comment of ride", example = "This ride was great!")
    String comment,

    @NotNull(message = "{validation.rated-by.null}")
    @Schema(description = "who rate", example = "PASSENGER")
    RatedBy ratedBy

) {
}