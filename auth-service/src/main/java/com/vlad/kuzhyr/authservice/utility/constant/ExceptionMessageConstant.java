package com.vlad.kuzhyr.authservice.utility.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessageConstant {

    public static final String USER_CREATE_EXCEPTION_MESSAGE = "Failed to create user in Keycloak. Status: %d";

    public static final String USER_ALREADY_EXISTS_EXCEPTION_MESSAGE = "User already exists by email: %d";

    public static final String USER_NOT_FOUND_BY_EMAIL_EXCEPTION_MESSAGE = "User not found by email: %s";

    public static final String USER_ALREADY_HAS_ROLE_MESSAGE = "User already has a role: %s";

    public static final String FAILED_REFRESH_TOKEN_MESSAGE = "Failed to refresh token. Status: %d";
}
