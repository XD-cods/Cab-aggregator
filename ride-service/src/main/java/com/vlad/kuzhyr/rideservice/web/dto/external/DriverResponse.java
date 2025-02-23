package com.vlad.kuzhyr.rideservice.web.dto.external;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;


@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DriverResponse(

    @Schema(description = "Driver id", example = "1")
    Long id,

    @Schema(description = "Driver first name", example = "John")
    String firstName,

    @Schema(description = "Driver last name", example = "Watson")
    String lastName,

    @Schema(description = "Driver email", example = "example@mail.ru")
    String email,

    @Schema(description = "Driver gender", example = "MALE")
    String gender,

    @Schema(description = "Driver phone number", example = "+375335184521")
    String phone,

    @Schema(description = "Driver cars id's", example = "[1,2]")
    List<Long> carIds,

    @Schema(description = "is enable flag", example = "true")
    Boolean isEnabled,

    @Schema(description = "is busy flag", example = "false")
    Boolean isBusy

) {
}