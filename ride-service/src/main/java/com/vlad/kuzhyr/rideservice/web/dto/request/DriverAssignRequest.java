package com.vlad.kuzhyr.rideservice.web.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DriverAssignRequest(

    @NotNull(message = "{validation.driver.id.null}")
    @Schema(description = "Assign driver id", example = "1")
    Long driverId

) {

    @Override
    public String toString() {
        return "DriverAssignRequest{" + "driverId=" + driverId + '}';
    }

}
