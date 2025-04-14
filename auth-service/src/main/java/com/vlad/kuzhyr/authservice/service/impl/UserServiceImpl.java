package com.vlad.kuzhyr.authservice.service.impl;


import com.vlad.kuzhyr.authservice.service.UserService;
import com.vlad.kuzhyr.authservice.utility.constant.BusinessRole;
import com.vlad.kuzhyr.authservice.utility.keycloak.KeycloakResponseHandler;
import com.vlad.kuzhyr.authservice.utility.keycloak.KeycloakUtil;
import com.vlad.kuzhyr.authservice.web.dto.request.SignInRequest;
import com.vlad.kuzhyr.authservice.web.dto.request.SignUpRequest;
import com.vlad.kuzhyr.authservice.web.dto.response.TokenResponse;
import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final KeycloakService keycloakService;
    private final Keycloak keycloakAdmin;
    private final KeycloakUtil keycloakUtil;
    private final KeycloakResponseHandler keycloakResponseHandler;

    @Value("${keycloak.realm}")
    private String realm;

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        UserRepresentation user = keycloakUtil.createUserRepresentation(signUpRequest);
        CredentialRepresentation credential = keycloakUtil.createPasswordCredential(signUpRequest.password());
        user.setCredentials(List.of(credential));

        UsersResource usersResource = keycloakUtil.getUsersResource();
        Response response = usersResource.create(user);

        keycloakResponseHandler.handleUserCreationResponse(response);

        String userId = keycloakUtil.extractUserIdFromResponse(response);

        BusinessRole roleToAssign = Optional.ofNullable(signUpRequest.businessRole())
            .orElse(BusinessRole.PASSENGER);

        assignRoleToUser(userId, roleToAssign);
    }

    private void assignRoleToUser(String userId, BusinessRole businessRole) {
        UsersResource usersResource = keycloakUtil.getUsersResource();
        UserResource userResource = usersResource.get(userId);

        List<RoleRepresentation> currentRoles = userResource.roles().realmLevel().listAll();

        boolean roleAlreadyAssigned = currentRoles.stream()
            .anyMatch(role -> role.getName().equals(businessRole.getKeycloakName()));

        if (!roleAlreadyAssigned) {
            RoleRepresentation roleToAdd = keycloakAdmin.realm(realm).roles()
                .get(businessRole.getKeycloakName()).toRepresentation();

            userResource.roles().realmLevel().add(Collections.singletonList(roleToAdd));
        }
    }

    @Override
    public TokenResponse signIn(SignInRequest signInRequest) {
        return keycloakService.signIn(signInRequest);
    }

    public void assignBusinessRoleByEmail(String email, BusinessRole businessRole) {
        UsersResource usersResource = keycloakUtil.getUsersResource();

        List<UserRepresentation> users = usersResource.searchByEmail(email, true);
        keycloakResponseHandler.handleUserSearchResponse(users, email);

        String userId = users.stream()
            .findFirst().get().getId();

        assignRoleToUser(userId, businessRole);
    }

}
