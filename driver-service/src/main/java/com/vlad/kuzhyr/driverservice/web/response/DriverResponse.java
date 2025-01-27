package com.vlad.kuzhyr.driverservice.web.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.util.List;


@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DriverResponse(

        Long id,

        String firstName,

        String lastName,

        String email,

        String gender,

        String phone,

        List<Long> carIds

) {
}