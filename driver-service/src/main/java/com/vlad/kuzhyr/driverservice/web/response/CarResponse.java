package com.vlad.kuzhyr.driverservice.web.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CarResponse(

        @Schema(description = "Car id", example = "1")
        Long id,

        @Schema(description = "Describes car color", example = "red")
        String color,

        @Schema(description = "Describes car brand ", example = "mercedes")
        String carBrand,

        @Schema(description = "Describes car number ", example = "К092НХ07")
        String carNumber,

        @Schema(description = "Existing driver id", example = "1")
        Long driverId

) {
}