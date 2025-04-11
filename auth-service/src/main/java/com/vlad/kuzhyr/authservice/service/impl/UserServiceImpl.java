package com.vlad.kuzhyr.authservice.service.impl;


import com.vlad.kuzhyr.authservice.service.UserService;
import com.vlad.kuzhyr.authservice.web.dto.request.SignUpRequest;
import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final Keycloak keycloakAdmin;

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        UserRepresentation user = getUserRepresentationByRequest(signUpRequest);
        CredentialRepresentation credential = getCredentialRepresentation(signUpRequest);
        user.setCredentials(List.of(credential));
        UsersResource usersResource = getUsersResource();
        Response response =  usersResource.create(user);
    }

    private UserRepresentation getUserRepresentationByRequest(SignUpRequest signUpRequest) {
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

    private CredentialRepresentation getCredentialRepresentation(SignUpRequest signUpRequest) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(signUpRequest.password());
        credential.setTemporary(false);

        return credential;
    }

    public UsersResource getUsersResource() {
        return keycloakAdmin.realm(realm).users();
    }

}
