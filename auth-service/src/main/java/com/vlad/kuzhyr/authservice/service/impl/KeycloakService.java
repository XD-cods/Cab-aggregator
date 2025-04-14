package com.vlad.kuzhyr.authservice.service.impl;

import com.vlad.kuzhyr.authservice.utility.constant.KeycloakConstant;
import com.vlad.kuzhyr.authservice.web.dto.request.SignInRequest;
import com.vlad.kuzhyr.authservice.web.dto.response.TokenResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    public TokenResponse signIn(SignInRequest signInRequest) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        requestBody.add(OAuth2Constants.CLIENT_ID, clientId);
        requestBody.add(OAuth2Constants.CLIENT_SECRET, clientSecret);
        requestBody.add(OAuth2Constants.USERNAME, signInRequest.email());
        requestBody.add(OAuth2Constants.PASSWORD, signInRequest.password());
        requestBody.add(OAuth2Constants.GRANT_TYPE, OAuth2Constants.PASSWORD);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        URI uri = UriComponentsBuilder
            .fromUriString(KeycloakConstant.getTokenUrl())
            .buildAndExpand()
            .encode(StandardCharsets.UTF_8)
            .toUri();

        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
            uri,
            new HttpEntity<>(requestBody, headers),
            TokenResponse.class
        );

        return response.getBody();
    }

}
