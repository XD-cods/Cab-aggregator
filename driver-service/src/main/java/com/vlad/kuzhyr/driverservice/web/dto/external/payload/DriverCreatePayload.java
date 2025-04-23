package com.vlad.kuzhyr.driverservice.web.dto.external.payload;

import lombok.Builder;

@Builder
public record DriverCreatePayload(

    String firstName,

    String lastName,

    String email,

    String phone,

    String keycloakId
) {
}
