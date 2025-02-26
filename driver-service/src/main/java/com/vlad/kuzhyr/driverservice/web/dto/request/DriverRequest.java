package com.vlad.kuzhyr.driverservice.web.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vlad.kuzhyr.driverservice.persistence.entity.Gender;
import com.vlad.kuzhyr.driverservice.utility.constant.RegularExpressionConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DriverRequest(

    @NotBlank(message = "{validation.firstname.empty}")
    @Schema(description = "Driver first name", example = "John")
    String firstName,

    @NotBlank(message = "{validation.lastname.empty}")
    @Schema(description = "Driver last name", example = "Watson")
    String lastName,

    @Email(message = "{validation.email.invalid}")
    @NotBlank(message = "{validation.email.empty}")
    @Schema(description = "Driver email", example = "example@mail.ru")
    String email,

    @NotNull(message = "{validation.gender.empty}")
    @Schema(description = "Driver gender", example = "MALE")
    Gender gender,

    @NotBlank(message = "{validation.phone.empty}")
    @Pattern(regexp = RegularExpressionConstant.DRIVER_PHONE_REGEX, message = "{validation.phone.invalid}")
    @Schema(description = "Driver phone number", example = "+375335184521")
    String phone,

    @Schema(description = "Driver cars id's", example = "[1,2]")
    List<Long> carIds

) {
}
