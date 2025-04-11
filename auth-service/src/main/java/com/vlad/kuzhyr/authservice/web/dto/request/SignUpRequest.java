package com.vlad.kuzhyr.authservice.web.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vlad.kuzhyr.authservice.utility.constant.RegularExpressionConstant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record SignUpRequest(

    @Schema(description = "Sign up first name", example = "Victor")
    @NotBlank(message = "{validation.firstname.empty}")
    String firstName,

    @Schema(description = "Sign up last name", example = "Don")
    @NotBlank(message = "{validation.lastname.empty}")
    String lastName,

    @NotEmpty//TODO: add important validation
    String password,

    @Email(message = "{validation.email.invalid}")
    @Schema(description = "Sign up email", example = "example@gmail.com")
    @NotBlank(message = "{validation.email.empty}")
    String email,

    @Schema(description = "Sign up phone", example = "+375336190975")
    @NotBlank(message = "{validation.phone.empty}")
    @Pattern(regexp = RegularExpressionConstant.PHONE_REG_XP, message = "{validation.phone.invalid}")
    String phone

) {
}
