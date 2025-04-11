package com.vlad.kuzhyr.authservice.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record SignInRequest(

    @Email(message = "{validation.email.invalid}")
    @Schema(description = "Sign up email", example = "example@gmail.com")
    @NotBlank(message = "{validation.email.empty}")
    String email,

    @NotEmpty//TODO: add important validation
    String password

) {
}
