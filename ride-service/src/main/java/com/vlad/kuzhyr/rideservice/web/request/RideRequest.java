package com.vlad.kuzhyr.rideservice.web.request;

import com.vlad.kuzhyr.rideservice.persistence.entity.RideStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record RideRequest(

        @NotBlank(message = "{validation.address.start.empty}")
        @Schema(description = "Ride start address", example = "Россия, г. Орехово-Зуево, Октябрьская ул., д. 3")
        String startAddress,

        @NotBlank(message = "{validation.address.finish.empty}")
        @Schema(description = "Ride finish address", example = "Россия, г. Орехово-Зуево, Октябрьская ул., д. 35")
        String finishAddress,

        @NotNull(message = "{validation.driver.id.null}")
        @Schema(description = "Ride driver id", example = "1")
        Long driverId,

        @NotNull(message = "{validation.passenger.id.null}")
        @Schema(description = "Ride passenger id", example = "1")
        Long passengerId,

        @NotNull(message = "{validation.status.empty}")
        @Schema(description = "Ride status id", example = "CREATED")
        RideStatus rideStatus,

        @NotNull(message = "{validation.price.null}")
        @Positive(message = "{validation.price.unpositive}")
        @Schema(description = "Ride price", example = "1000")
        BigDecimal ridePrice

) {
}