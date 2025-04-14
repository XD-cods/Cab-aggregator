package com.vlad.kuzhyr.authservice.utility.keycloak;

import com.vlad.kuzhyr.authservice.web.dto.request.SignUpRequest;
import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
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

    public UsersResource getUsersResource() {
        return keycloakAdmin.realm(realm).users();
    }

    public UserResource getUserResource(String userId) {
        return getUsersResource().get(userId);
    }

    public UserRepresentation createUserRepresentation(
        SignUpRequest signUpRequest
    ) {

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