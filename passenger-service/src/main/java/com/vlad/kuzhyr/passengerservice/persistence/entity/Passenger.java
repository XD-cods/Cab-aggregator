package com.vlad.kuzhyr.passengerservice.persistence.entity;

import com.vlad.kuzhyr.passengerservice.utility.constant.PassengerConstant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "passenger")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Passenger {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "passenger_id", unique = true, nullable = false)
  private Long id;

  @NotNull
  @NotEmpty(message = "Last name can't be empty")
  private String lastName;

  @NotNull
  @NotEmpty(message = "First name can't be empty")
  private String firstName;

  @Email
  @NotEmpty(message = "Phone can't be empty")
  private String email;

  @NotNull
  @NotEmpty(message = "Phone can't be empty")
  @Pattern(regexp = PassengerConstant.PHONE_REG_XP, message = "Phone not a valid")
  private String phone;

  @Builder.Default
  @NotNull
  private Boolean isEnabled = Boolean.TRUE;
}
