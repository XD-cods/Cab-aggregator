package com.vlad.kuzhyr.authservice.web.dto.request;

import com.vlad.kuzhyr.authservice.utility.constant.RegularExpressionConstant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record SignInRequest(

    @Email(message = "{validation.email.invalid}")
    @Schema(description = "Sign up email", example = "example@gmail.com")
    @NotBlank(message = "{validation.email.empty}")
    String email,

    @Schema(description = "Sign up password", example = "P@ssw0rd")
    @NotBlank(message = "{validation.password.empty}")
    @Size(min = 6, message = "{validation.password.empty}")
    @Pattern(
        regexp = RegularExpressionConstant.PASSWORD_REG_XP,
        message = "{validation.password.invalid}"
    )
    String password

) {
}
