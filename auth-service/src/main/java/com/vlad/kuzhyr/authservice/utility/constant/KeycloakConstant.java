package com.vlad.kuzhyr.authservice.utility.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KeycloakConstant {

    private static String serverUrl;
    private static String realm;

    @Value("${keycloak.auth-server-url}")
    public void setServerUrl(String url) {
        serverUrl = url;
    }

    @Value("${keycloak.realm}")
    public void setRealm(String realmName) {
        realm = realmName;
    }

    public static String getTokenUrl() {
        return String.format("%s/realms/%s/protocol/openid-connect/token", serverUrl, realm);
    }

}
