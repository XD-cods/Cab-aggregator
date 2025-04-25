package com.vlad.kuzhyr.authservice.service.impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vlad.kuzhyr.authservice.exception.KeycloakOperationException;
import com.vlad.kuzhyr.authservice.exception.KeycloakUserNotFoundException;
import com.vlad.kuzhyr.authservice.exception.UserAlreadyHasRole;
import com.vlad.kuzhyr.authservice.persistence.entity.Gender;
import com.vlad.kuzhyr.authservice.service.KeycloakAuthService;
import com.vlad.kuzhyr.authservice.service.UserService;
import com.vlad.kuzhyr.authservice.utility.client.DriverFeignClient;
import com.vlad.kuzhyr.authservice.utility.client.PassengerFeignClient;
import com.vlad.kuzhyr.authservice.utility.constant.BusinessRole;
import com.vlad.kuzhyr.authservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.authservice.utility.keycloak.KeycloakUtil;
import com.vlad.kuzhyr.authservice.utility.logger.LogUtils;
import com.vlad.kuzhyr.authservice.web.dto.external.DriverRequest;
import com.vlad.kuzhyr.authservice.web.dto.external.PassengerRequest;
import com.vlad.kuzhyr.authservice.web.dto.request.RefreshRequest;
import com.vlad.kuzhyr.authservice.web.dto.request.SignInRequest;
import com.vlad.kuzhyr.authservice.web.dto.request.SignUpRequest;
import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final KeycloakUtil keycloakUtil;
    private final ObjectMapper objectMapper;
    private final DriverFeignClient driverFeignClient;
    private final PassengerFeignClient passengerFeignClient;
    private final KeycloakAuthService keycloakAuthService;

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        log.debug("signUp. Entering method. {}", signUpRequest);

        Response response = createNewUser(signUpRequest);
        handleUserCreationResponse(response);
        String userId = keycloakUtil.extractUserIdFromResponse(response);

        String roleToAssign = Optional.ofNullable(signUpRequest.businessRole())
            .orElse(BusinessRole.PASSENGER).getKeycloakName();

        assignRole(userId, roleToAssign);
        log.info("signUp. User successfully created. {}", signUpRequest);
    }

    @Override
    public AccessTokenResponse signIn(SignInRequest signInRequest) {
        log.debug("signIn. Entering method. {}", signInRequest);

        Keycloak keycloak = keycloakUtil.buildKeycloakClient(signInRequest);

        log.info("signIn. The user has successfully entered. {}", signInRequest);
        return keycloak.tokenManager().getAccessToken();
    }

    @Override
    public void assignBusinessRoleByEmail(String email, BusinessRole businessRole) {
        String maskEmail = LogUtils.maskEmail(email);

        log.debug("assignBusinessRoleByEmail. Entering method. User email: {}, new role: {}", maskEmail, businessRole);

        UserRepresentation userRepresentation = keycloakUtil.getUsersResource()
            .searchByEmail(email, true)
            .stream()
            .findFirst()
            .orElseThrow(() -> new KeycloakUserNotFoundException(
                ExceptionMessageConstant.USER_NOT_FOUND_BY_EMAIL_EXCEPTION_MESSAGE.formatted(email)
            ));

        String userId = userRepresentation.getId();

        assignRole(userId, businessRole.getKeycloakName());
        log.info("assignBusinessRoleByEmail. User successfully get new role. User email: {}, new role: {}", maskEmail,
            businessRole);

    }

    @Override
    public void logout(RefreshRequest refreshRequest) {
        log.debug("logout. Entering method. Refresh token: {}", refreshRequest);

        keycloakAuthService.logout(refreshRequest.refreshToken());

        log.info("logout. User successfully logged out. {}", refreshRequest);
    }

    @Override
    public AccessTokenResponse refreshToken(RefreshRequest refreshRequest) {
        log.debug("refreshToken. Entering method. Refresh token: {}", refreshRequest);

        AccessTokenResponse response = keycloakAuthService.refreshToken(refreshRequest.refreshToken());

        log.info("refreshToken. Token refreshing successfully completed. {}", refreshRequest);
        return response;
    }

    private void assignRole(String userId, String keycloakRole) {
        if (hasRole(userId, keycloakRole)) {
            log.info("assignRole. User already has role. User id: {}, role: {}", userId, keycloakRole);
            throw new UserAlreadyHasRole(
                ExceptionMessageConstant.USER_ALREADY_HAS_ROLE_MESSAGE.formatted(keycloakRole)
            );
        }

        UserResource userResource = keycloakUtil.getUsersResource().get(userId);

        RoleRepresentation roleRepresentation = keycloakUtil.getRoleResource(keycloakRole).toRepresentation();

        userResource
            .roles()
            .realmLevel()
            .add(Collections.singletonList(roleRepresentation));

        if (BusinessRole.DRIVER.getKeycloakName().equals(keycloakRole)) {
            sendDriverCreateRequest(userId);
        } else if (BusinessRole.PASSENGER.getKeycloakName().equals(keycloakRole)) {
            sendPassengerCreateRequest(userId);
        }
    }

    private Response createNewUser(SignUpRequest signUpRequest) {
        UserRepresentation user = keycloakUtil.createUserRepresentation(signUpRequest);
        CredentialRepresentation credential = keycloakUtil.createPasswordCredential(signUpRequest.password());
        user.setCredentials(List.of(credential));

        UsersResource usersResource = keycloakUtil.getUsersResource();
        Response response = usersResource.create(user);

        return response;
    }

    private void handleUserCreationResponse(Response response) {
        HttpStatus status = HttpStatus.valueOf(response.getStatus());
        String errorMessage = getResponseBodyMessage(response);

        if (status != HttpStatus.CREATED) {
            log.error("handleUserCreationResponse. Threw new error. Error message: {}", errorMessage);
            throw new KeycloakOperationException(errorMessage, status);
        }
    }

    private String getResponseBodyMessage(Response response) {
        try {
            if (response.hasEntity()) {
                String body = response.readEntity(String.class);
                if (body.startsWith("{")) {
                    JsonNode json = objectMapper.readTree(body);
                    if (json.has("errorMessage")) {
                        return json.get("errorMessage").asText();
                    }
                }
                return body;
            }
        } catch (Exception e) {
            log.warn("Failed to parse Keycloak response body", e);
        }
        return null;
    }

    private boolean hasRole(String userId, String roleName) {
        UserResource userResource = keycloakUtil.getUsersResource().get(userId);
        List<RoleRepresentation> currentRoles = userResource.roles().realmLevel().listAll();

        return currentRoles.stream()
            .anyMatch(role -> role.getName().equals(roleName));
    }

    private void sendDriverCreateRequest(String userId) {
        log.debug("sendDriverCreateRequest. Entering method. User id: {}", userId);
        UserRepresentation user = keycloakUtil.getUsersResource().get(userId).toRepresentation();
        DriverRequest driverRequest = DriverRequest.builder()
            .keycloakId(userId)
            .gender(Gender.UNKNOWN)
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .carIds(List.of())
            .phone(getPhone(user))
            .build();

        driverFeignClient.createDriver(driverRequest);
        log.debug("sendDriverCreateRequest. Driver create request successfully sent. User id: {}", userId);
    }

    private void sendPassengerCreateRequest(String userId) {
        log.debug("sendPassengerCreateRequest. Entering method. User id: {}", userId);
        UserRepresentation user = keycloakUtil.getUsersResource().get(userId).toRepresentation();
        PassengerRequest driverRequest = PassengerRequest.builder()
            .keycloakId(userId)
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .phone(getPhone(user))
            .build();

        passengerFeignClient.createPassenger(driverRequest);
        log.debug("sendPassengerCreateRequest. Passenger create request successfully sent. User id: {}", userId);
    }

    private String getPhone(UserRepresentation user) {
        Map<String, List<String>> attributes = user.getAttributes();
        if (attributes == null || attributes.get("phone") == null || attributes.get("phone").isEmpty()) {
            log.warn("Phone number not found for user {}", user.getId());
            return null;
        }
        return attributes.get("phone").get(0);
    }

}
