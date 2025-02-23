package com.vlad.kuzhyr.driverservice.web.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CarRequest(

    @NotBlank(message = "{validation.color.empty}")
    @Schema(description = "Describes car color ", example = "red")
    String color,

    @NotBlank(message = "{validation.brand.empty}")
    @Schema(description = "Describes car brand ", example = "mercedes")
    String carBrand,

    @NotBlank(message = "{validation.carnumber.empty}")
    @Schema(description = "Describes car number ", example = "К092НХ07")
    String carNumber

) {

}