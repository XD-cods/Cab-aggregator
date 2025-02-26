package com.vlad.kuzhyr.passengerservice.web.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vlad.kuzhyr.passengerservice.utility.constant.RegularExpressionConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PassengerRequest(

    @Schema(description = "Passenger first name", example = "Victor")
    @NotBlank(message = "{validation.firstname.empty}")
    String firstName,

    @Schema(description = "Passenger last name", example = "Don")
    @NotBlank(message = "{validation.lastname.empty}")
    String lastName,

    @Email(message = "{validation.email.invalid}")
    @Schema(description = "Passenger email", example = "example@gmail.com")
    @NotBlank(message = "{validation.email.empty}")
    String email,

    @Schema(description = "Passenger phone", example = "7849232")
    @NotBlank(message = "{validation.phone.empty}")
    @Pattern(regexp = RegularExpressionConstant.PHONE_REG_XP, message = "{validation.phone.invalid}")
    String phone

) {
}
