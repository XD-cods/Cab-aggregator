package com.vlad.kuzhyr.rideservice.web.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateRideRequest(

        @NotBlank(message = "{validation.address.departure.empty}")
        String departureAddress,

        @NotBlank(message = "{validation.address.destination.empty}")
        String destinationAddress

) {

}