package com.vlad.kuzhyr.driverservice.persistence.entity;

import com.vlad.kuzhyr.driverservice.utility.constant.DriverConstant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "driver")
public class Driver {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @NotEmpty(message = "A first name can't be empty")
  @Column(nullable = false)
  private String firstName;

  @NotEmpty(message = "A last name can't be empty")
  @Column(nullable = false)
  private String lastName;

  @NotEmpty(message = "A email can't be empty")
  @Column(unique = true, nullable = false)
  private String email;

  @NotEmpty(message = "A gender can't be empty")
  @Column(nullable = false)
  private String gender;

  @NotEmpty(message = "A phone can't be empty")
  @Pattern(regexp = DriverConstant.PASSENGER_PHONE_REGEX)
  @Column(unique = true, nullable = false)
  private String phone;

  @Column(nullable = false)
  private Long carId;
}