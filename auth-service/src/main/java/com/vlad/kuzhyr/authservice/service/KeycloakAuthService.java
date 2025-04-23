package com.vlad.kuzhyr.authservice.service;

import jakarta.validation.constraints.NotBlank;
import org.keycloak.representations.AccessTokenResponse;

public interface KeycloakAuthService {

    AccessTokenResponse refreshToken(String refreshToken);

    void logout(String refreshToken);

}
