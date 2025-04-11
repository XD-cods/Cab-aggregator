package com.vlad.kuzhyr.authservice.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

//    final static String userName = "YOUR_REALM_ADMIN_USERNAME";
//    final static String password = "Your_REALM_ADMIN_PASSWORD";

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    @Bean
    public Keycloak keycloakAdmin() {
        return KeycloakBuilder.builder()
            .serverUrl(serverUrl)
            .realm(realm)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .build();
//            .username("admin")
//            .password("admin")
    }
}