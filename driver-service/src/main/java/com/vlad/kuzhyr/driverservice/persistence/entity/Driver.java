package com.vlad.kuzhyr.driverservice.persistence.entity;

import com.vlad.kuzhyr.driverservice.utility.constant.RegularPatternConstant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
  @Column(name = "driver_id", nullable = false)
  private Long id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "gender", nullable = false)
  private String gender;

  @Pattern(regexp = RegularPatternConstant.PASSENGER_PHONE_REGEX)
  @Column(name = "phone", unique = true, nullable = false)
  private String phone;

  @Column(name = "car_id", unique = true)
  private Long carId;

  @Builder.Default
  @Column(name = "is_enabled", nullable = false)
  private Boolean isEnabled = Boolean.TRUE;

}