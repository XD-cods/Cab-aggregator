package com.vlad.kuzhyr.authservice.service;

import com.vlad.kuzhyr.authservice.web.dto.request.SignInRequest;
import com.vlad.kuzhyr.authservice.web.dto.request.SignUpRequest;
import com.vlad.kuzhyr.authservice.web.dto.response.TokenResponse;

public interface UserService {

    void signUp(SignUpRequest signUpRequest);

    TokenResponse signIn(SignInRequest signInRequest);

}
