package com.vlad.kuzhyr.authservice.utility.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessageConstant {

    public static final String USER_CREATE_EXCEPTION_MESSAGE = "User creation failed %d";

    public static final String USER_ALREADY_EXISTS_EXCEPTION_MESSAGE = "User already exists by email: %d";

}
