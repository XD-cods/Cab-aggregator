package com.vlad.kuzhyr.authservice.web.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vlad.kuzhyr.authservice.utility.constant.BusinessRole;
import com.vlad.kuzhyr.authservice.utility.constant.RegularExpressionConstant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SignUpRequest(

    @Schema(description = "Sign up first name", example = "Victor")
    @NotBlank(message = "{validation.firstname.empty}")
    String firstName,

    @Schema(description = "Sign up last name", example = "Don")
    @NotBlank(message = "{validation.lastname.empty}")
    String lastName,

    @Schema(description = "Sign up password", example = "P@ssw0rd")
    @NotBlank(message = "{validation.password.empty}")
    @Size(min = 6, message = "{validation.password.empty}")
    @Pattern(
        regexp = RegularExpressionConstant.PASSWORD_REG_XP,
        message = "{validation.password.invalid}"
    )
    String password,

    @Schema(description = "Sign up email", example = "example@gmail.com")
    @Email(message = "{validation.email.invalid}")
    @NotBlank(message = "{validation.email.empty}")
    String email,

    @Schema(description = "Sign up phone", example = "+375336190975")
    @NotBlank(message = "{validation.phone.empty}")
    @Pattern(regexp = RegularExpressionConstant.PHONE_REG_XP, message = "{validation.phone.invalid}")
    String phone,

    BusinessRole businessRole

) {
}
