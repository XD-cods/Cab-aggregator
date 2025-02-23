package com.vlad.kuzhyr.ratingservice.web.dto.external;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RideInfoPayload(

    Long rideId,

    Long driverId,

    Long passengerId

) {
}