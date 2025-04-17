package com.vlad.kuzhyr.authservice.utility.keycloak;

import com.vlad.kuzhyr.authservice.web.dto.request.SignInRequest;
import com.vlad.kuzhyr.authservice.web.dto.request.SignUpRequest;
import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeycloakUtil {

    private final Keycloak keycloakAdmin;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    public RealmResource getRealmResource() {
        return keycloakAdmin.realm(realm);
    }

    public UsersResource getUsersResource() {
        return getRealmResource().users();
    }

    public UserResource getUserResource(String userId) {
        return getUsersResource().get(userId);
    }

    public RoleResource getRoleResource(String roleName) {
        return getRealmResource().roles().get(roleName);
    }

    public UserRepresentation createUserRepresentation(SignUpRequest signUpRequest) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setEmail(signUpRequest.email());
        user.setFirstName(signUpRequest.firstName());
        user.setLastName(signUpRequest.lastName());
        user.setEmailVerified(false);

        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("phone", Collections.singletonList(signUpRequest.phone()));
        user.setAttributes(attributes);

        return user;
    }

    public Keycloak buildKeycloakClient(SignInRequest signInRequest) {

        Keycloak keycloak = KeycloakBuilder.builder()
            .serverUrl(serverUrl)
            .realm(realm)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .username(signInRequest.email())
            .password(signInRequest.password())
            .grantType(OAuth2Constants.PASSWORD)
            .build();

        return keycloak;
    }

    public CredentialRepresentation createPasswordCredential(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        return credential;
    }

    public String extractUserIdFromResponse(Response response) {
        String location = response.getLocation().toString();
        return location.substring(location.lastIndexOf('/') + 1);
    }

}