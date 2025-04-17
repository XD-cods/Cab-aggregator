package com.vlad.kuzhyr.passengerservice.web.dto.external;

import lombok.Builder;

@Builder
public record PassengerCreatePayload(

    String firstName,

    String lastName,

    String email,

    String phone

) {
}
