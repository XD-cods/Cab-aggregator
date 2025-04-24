package com.vlad.kuzhyr.authservice.web.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vlad.kuzhyr.authservice.utility.constant.BusinessRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AssignBusinessRoleRequest(

    @NotBlank(message = "{validation.email.empty}")
    @Email(message = "{validation.email.invalid}")
    String email,

    @NotNull(message = "{validation.role.null}")
    BusinessRole businessRole

) {
}
