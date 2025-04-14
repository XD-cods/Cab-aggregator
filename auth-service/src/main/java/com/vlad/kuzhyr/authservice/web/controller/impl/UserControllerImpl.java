package com.vlad.kuzhyr.authservice.web.controller.impl;

import com.vlad.kuzhyr.authservice.service.UserService;
import com.vlad.kuzhyr.authservice.web.controller.UserController;
import com.vlad.kuzhyr.authservice.web.dto.request.AssignBusinessRoleRequest;
import com.vlad.kuzhyr.authservice.web.dto.request.SignInRequest;
import com.vlad.kuzhyr.authservice.web.dto.request.SignUpRequest;
import com.vlad.kuzhyr.authservice.web.dto.response.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
@Validated
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        userService.signUp(signUpRequest);
    }

    @Override
    @PostMapping("/signin")
    public ResponseEntity<TokenResponse> singIn(@RequestBody @Valid SignInRequest signInRequest) {
        TokenResponse responseBody = userService.signIn(signInRequest);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/assign-business-role")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public void assignBusinessRoleByEmail(@RequestBody @Valid AssignBusinessRoleRequest request) {
        userService.assignBusinessRoleByEmail(request.email(), request.businessRole());
    }

}
