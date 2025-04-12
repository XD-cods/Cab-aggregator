package com.vlad.kuzhyr.authservice.web.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TokenResponse(

    String accessToken,

    String refreshToken,

    Long expiresIn,

    Long refreshExpiresIn,

    String tokenType

) {
}
