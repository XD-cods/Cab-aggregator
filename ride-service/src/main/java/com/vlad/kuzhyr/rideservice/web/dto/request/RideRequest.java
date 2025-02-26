package com.vlad.kuzhyr.rideservice.web.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RideRequest(

    @NotBlank(message = "{validation.address.departure.empty}")
    @Schema(description = "Ride start address", example = "Россия, г. Орехово-Зуево, Октябрьская ул., д. 3")
    String departureAddress,

    @NotBlank(message = "{validation.address.destination.empty}")
    @Schema(description = "Ride finish address", example = "Россия, г. Орехово-Зуево, Октябрьская ул., д. 35")
    String destinationAddress,

    @NotNull(message = "{validation.driver.id.null}")
    @Schema(description = "Ride driver id", example = "1")
    Long driverId,

    @NotNull(message = "{validation.passenger.id.null}")
    @Schema(description = "Ride passenger id", example = "1")
    Long passengerId

) {
    @Override
    public String toString() {
        return "RideRequest{" +
               "passengerId=" + passengerId +
               ", driverId=" + driverId +
               '}';
    }
}
