package com.vlad.kuzhyr.rideservice.web.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vlad.kuzhyr.rideservice.persistence.entity.RideStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RideResponse(

        @Schema(description = "Ride id", example = "1")
        Long id,

        @Schema(description = "Ride start address", example = "Россия, г. Орехово-Зуево, Октябрьская ул., д. 3")
        String startAddress,

        @Schema(description = "Ride finish address", example = "Россия, г. Орехово-Зуево, Октябрьская ул., д. 35")
        String finishAddress,

        @Schema(description = "Ride driver id", example = "1")
        Long driverId,

        @Schema(description = "Ride passenger id", example = "1")
        Long passengerId,

        @Schema(description = "Ride distance", example = "1000")
        Double rideDistance,

        @Schema(description = "Ride status", example = "CREATED")
        RideStatus rideStatus,

        @Schema(description = "Ride price", example = "1000")
        BigDecimal ridePrice,

        @Schema(description = "Ride start time", example = "2024-10-15 14:30")
        LocalDateTime startTime,

        @Schema(description = "Ride finish time", example = "2024-10-15 14:50")
        LocalDateTime finishTime

) {

}