package com.vlad.kuzhyr.authservice.utility.keycloak;

import com.vlad.kuzhyr.authservice.exception.KeycloakOperationException;
import com.vlad.kuzhyr.authservice.exception.KeycloakUserNotFoundException;
import com.vlad.kuzhyr.authservice.utility.constant.ExceptionMessageConstant;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class KeycloakResponseHandler {

    public void handleUserCreationResponse(Response response) {
        int responseStatus = response.getStatus();

        if (responseStatus != HttpStatus.CREATED.value()) {
            throw new KeycloakOperationException(
                ExceptionMessageConstant.USER_CREATE_EXCEPTION_MESSAGE.formatted(responseStatus),
                responseStatus
            );
        }
    }

    public void handleUserSearchResponse(List<UserRepresentation> users, String email) {
        Optional.ofNullable(users)
            .flatMap(list -> list.stream().findFirst())
            .orElseThrow(() -> new KeycloakUserNotFoundException(
                ExceptionMessageConstant.USER_NOT_FOUND_BY_EMAIL_EXCEPTION_MESSAGE.formatted(email)
            ));
    }

}
