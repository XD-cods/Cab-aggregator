package com.vlad.kuzhyr.authservice.web.controller;

import com.vlad.kuzhyr.authservice.web.dto.request.AssignBusinessRoleRequest;
import com.vlad.kuzhyr.authservice.web.dto.request.RefreshRequest;
import com.vlad.kuzhyr.authservice.web.dto.request.SignInRequest;
import com.vlad.kuzhyr.authservice.web.dto.request.SignUpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Validated
@Tag(name = "Auth API", description = "api for authentication")
public interface UserController {

    @Operation(summary = "Register new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created"),
        @ApiResponse(responseCode = "400", description = "Not a valid field, error"),
        @ApiResponse(responseCode = "409", description = "User already exists with same email"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    void signUp(@Valid @RequestBody SignUpRequest signUpRequest);

    @Operation(summary = "Sign in")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User founded"),
        @ApiResponse(responseCode = "404", description = "User not founded"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<AccessTokenResponse> singIn(@Valid @RequestBody SignInRequest signInRequest);

    @Operation(summary = "Create new user with new business role")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User founded"),
        @ApiResponse(responseCode = "404", description = "User not founded"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    void assignBusinessRoleToUser(@RequestBody @Valid AssignBusinessRoleRequest request);

    @Operation(summary = "Refresh token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "token refreshed"),
        @ApiResponse(responseCode = "401", description = "token invalid"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<AccessTokenResponse> refresh(@RequestBody @Valid RefreshRequest refreshRequest);

    @Operation(summary = "Log out by refresh token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "log out successfully"),
        @ApiResponse(responseCode = "401", description = "token invalid"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    void logout(@RequestBody @Valid RefreshRequest request);
}
