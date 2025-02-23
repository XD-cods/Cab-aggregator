package com.vlad.kuzhyr.passengerservice.web.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PassengerResponse(

    @Schema(description = "Id of passenger", example = "1")
    Long id,

    @Schema(description = "Passenger first name", example = "Victor")
    String firstName,

    @Schema(description = "Passenger last name", example = "Don")
    String lastName,

    @Schema(description = "Passenger email", example = "example@gmail.com")
    String email,

    @Schema(description = "Passenger phone", example = "+375295162318")
    String phone,

    @Schema(description = "Passenger enabled", example = "true")
    Boolean isEnabled,

    @Schema(description = "Passenger busy", example = "false")
    Boolean isBusy

) {
}
