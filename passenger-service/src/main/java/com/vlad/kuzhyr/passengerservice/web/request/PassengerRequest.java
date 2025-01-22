package com.vlad.kuzhyr.passengerservice.web.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PassengerRequest {

  @Schema(description = "Passenger first name", example = "Victor")
  private String firstName;

  @Schema(description = "Passenger last name", example = "Don")
  private String lastName;

  @Schema(description = "Passenger email", example = "example@gmail.com")
  private String email;

  @Schema(description = "Passenger phone", example = "7849232")
  private String phone;
}
