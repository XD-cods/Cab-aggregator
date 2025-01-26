package com.vlad.kuzhyr.driverservice.web.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CarRequest(

        @NotBlank(message = "Color can't be empty")
        String color,

        @NotBlank(message = "Car brand can't be empty")
        String carBrand,

        @NotBlank(message = "Car number can't be empty")
        String carNumber

) {
}