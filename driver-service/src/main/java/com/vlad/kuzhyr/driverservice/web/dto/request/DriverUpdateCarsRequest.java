package com.vlad.kuzhyr.driverservice.web.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Builder;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record DriverUpdateCarsRequest(

    List<Long> carIds

) {
    @Override
    public String toString() {
        return "DriverUpdateCarsRequest{" +
               "carIds=" + carIds +
               '}';
    }
}
