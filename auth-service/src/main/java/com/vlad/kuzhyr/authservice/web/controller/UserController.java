package com.vlad.kuzhyr.authservice.web.controller;

import com.vlad.kuzhyr.authservice.web.dto.request.SignInRequest;
import com.vlad.kuzhyr.authservice.web.dto.request.SignUpRequest;
import com.vlad.kuzhyr.authservice.web.dto.response.TokenResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
public interface UserController {

    void signUp(@Valid @RequestBody SignUpRequest signUpRequest);

    ResponseEntity<TokenResponse> singIn(@Valid @RequestBody SignInRequest signInRequest);
}
