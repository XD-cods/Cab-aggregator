package com.vlad.kuzhyr.authservice.web.dto.request;

import com.vlad.kuzhyr.authservice.utility.logger.LogUtils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record SignInRequest(

    @Email(message = "{validation.email.invalid}")
    @Schema(description = "Sign up email", example = "example@gmail.com")
    @NotBlank(message = "{validation.email.empty}")
    String email,

    @Schema(description = "Sign up password", example = "P@ssw0rd")
    @NotBlank(message = "{validation.password.empty}")
    String password



) {
    @Override
    public String toString() {
        String maskEmail = LogUtils.maskEmail(email);

        return "SignInRequest{" +
               "email='" + maskEmail + '\'' +
               '}';
    }
}
