package com.vlad.kuzhyr.authservice.service;

import com.vlad.kuzhyr.authservice.utility.constant.BusinessRole;
import com.vlad.kuzhyr.authservice.web.dto.request.RefreshRequest;
import com.vlad.kuzhyr.authservice.web.dto.request.SignInRequest;
import com.vlad.kuzhyr.authservice.web.dto.request.SignUpRequest;
import jakarta.validation.Valid;
import org.keycloak.representations.AccessTokenResponse;

public interface UserService {

    void signUp(SignUpRequest signUpRequest);

    AccessTokenResponse signIn(SignInRequest signInRequest);

    void assignBusinessRoleByEmail(String email, BusinessRole businessRole);

    void logout(RefreshRequest request);

    AccessTokenResponse refreshToken(RefreshRequest refreshRequest);
}
