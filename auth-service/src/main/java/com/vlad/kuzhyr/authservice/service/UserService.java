package com.vlad.kuzhyr.authservice.service;

import com.vlad.kuzhyr.authservice.web.dto.request.SignUpRequest;

public interface UserService {

    void signUp(SignUpRequest signUpRequest);

}
