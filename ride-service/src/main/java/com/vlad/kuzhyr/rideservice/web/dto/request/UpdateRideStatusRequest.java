package com.vlad.kuzhyr.rideservice.web.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vlad.kuzhyr.rideservice.persistence.entity.RideStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateRideStatusRequest(

    @NotNull(message = "{validation.status.empty}")
    RideStatus rideStatus

) {

}
