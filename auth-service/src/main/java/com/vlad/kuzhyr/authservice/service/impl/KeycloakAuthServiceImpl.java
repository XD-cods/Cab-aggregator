package com.vlad.kuzhyr.authservice.service.impl;

import com.vlad.kuzhyr.authservice.exception.KeycloakOperationException;
import com.vlad.kuzhyr.authservice.service.KeycloakAuthService;
import com.vlad.kuzhyr.authservice.utility.constant.ExceptionMessageConstant;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KeycloakAuthServiceImpl implements KeycloakAuthService {

    private final RestTemplate restTemplate;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    @Override
    public AccessTokenResponse refreshToken(String refreshToken) {
        String tokenUrl = getTokenUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add(OAuth2Constants.GRANT_TYPE, OAuth2Constants.REFRESH_TOKEN);
        requestBody.add(OAuth2Constants.CLIENT_ID, clientId);
        requestBody.add(OAuth2Constants.CLIENT_SECRET, clientSecret);
        requestBody.add(OAuth2Constants.REFRESH_TOKEN, refreshToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity =
            new HttpEntity<>(requestBody, headers);

        ResponseEntity<AccessTokenResponse> response = restTemplate.exchange(
            tokenUrl,
            HttpMethod.POST,
            requestEntity,
            AccessTokenResponse.class
        );

        HttpStatus statusCode = HttpStatus.valueOf(response.getStatusCode().value());

        if (statusCode == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new KeycloakOperationException(
                ExceptionMessageConstant.FAILED_REFRESH_TOKEN_MESSAGE, statusCode
            );
        }
    }

    @Override
    public void logout(String refreshToken) {
        String revokeUrl = getRevokeUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(OAuth2Constants.CLIENT_ID, clientId);
        body.add(OAuth2Constants.CLIENT_SECRET, clientSecret);
        body.add(OAuth2Constants.TOKEN, refreshToken);
        body.add("token_type_hint", OAuth2Constants.REFRESH_TOKEN);

        restTemplate.postForEntity(revokeUrl, new HttpEntity<>(body, headers), Void.class);
    }

    private String getRevokeUrl() {
        return String.format("%s/realms/%s/protocol/openid-connect/revoke",
            authServerUrl, realm);
    }

    private String getTokenUrl() {
        return String.format("%s/realms/%s/protocol/openid-connect/token",
            authServerUrl, realm);
    }
}