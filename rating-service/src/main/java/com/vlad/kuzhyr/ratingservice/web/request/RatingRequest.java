package com.vlad.kuzhyr.ratingservice.web.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RatingRequest(

    @NotNull(message = "{validation.rating.null}")
    Long rideId,

    @NotNull(message = "{validation.rating.null}")
    @Min(value = 0, message = "{validation.rating.small}")
    @Max(value = 5, message = "{validation.rating.over}")
    Float rating,

    String comment

) {

}