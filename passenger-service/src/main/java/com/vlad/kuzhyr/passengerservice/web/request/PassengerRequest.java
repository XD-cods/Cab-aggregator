package com.vlad.kuzhyr.passengerservice.web.request;

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
        @NotBlank(message = "First name can't be empty")
        String firstName,

        @Schema(description = "Passenger last name", example = "Don")
        @NotBlank(message = "Last name can't be empty")
        String lastName,

        @Email
        @Schema(description = "Passenger email", example = "example@gmail.com")
        @NotBlank(message = "email can't be empty")
        String email,

        @Schema(description = "Passenger phone", example = "7849232")
        @NotBlank(message = "Phone can't be empty")
        @Pattern(regexp = RegularExpressionConstant.PHONE_REG_XP, message = "Phone not a valid")
        String phone

) {
}