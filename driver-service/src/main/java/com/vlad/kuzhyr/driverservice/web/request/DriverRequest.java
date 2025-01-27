package com.vlad.kuzhyr.driverservice.web.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vlad.kuzhyr.driverservice.utility.constant.RegularExpressionConstant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DriverRequest(

        @NotBlank(message = "First name can't be empty")
        String firstName,

        @NotBlank(message = "Last name can't be empty")
        String lastName,

        @Email(message = "email not a valid")
        @NotBlank(message = "Email can't be empty")
        String email,

        @NotBlank(message = "Gender can't be empty")
        String gender,

        @NotBlank(message = "Phone can't be empty")
        @Pattern(regexp = RegularExpressionConstant.PASSENGER_PHONE_REGEX, message = "Phone not a valid")
        String phone,

        Long carId

) {
}