package com.vlad.kuzhyr.driverservice.web.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
