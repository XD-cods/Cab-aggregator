package com.vlad.kuzhyr.authservice.web.controller;

import com.vlad.kuzhyr.authservice.web.dto.request.AssignBusinessRoleRequest;
import com.vlad.kuzhyr.authservice.web.dto.request.SignInRequest;
import com.vlad.kuzhyr.authservice.web.dto.request.SignUpRequest;
import jakarta.validation.Valid;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
public interface UserController {

    void signUp(@Valid @RequestBody SignUpRequest signUpRequest);

    ResponseEntity<AccessTokenResponse> singIn(@Valid @RequestBody SignInRequest signInRequest);

    void assignBusinessRoleToUser(@RequestBody @Valid AssignBusinessRoleRequest request);

}
