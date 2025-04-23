package com.vlad.kuzhyr.authservice.web.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RefreshRequest(

    @NotBlank(message = "{validation.token.empty}")
    String refreshToken

) {

    @Override
    public String toString() {
        if (refreshToken == null) {
            return "RefreshRequest{refreshToken=null}";
        }

        int substringLength = Math.min(refreshToken.length(), 50);
        return "RefreshRequest{refreshToken='" + refreshToken.substring(0, substringLength) + "...'}";
    }

}
