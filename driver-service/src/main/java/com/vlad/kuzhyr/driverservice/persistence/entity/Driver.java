package com.vlad.kuzhyr.driverservice.persistence.entity;

import com.vlad.kuzhyr.driverservice.utility.constant.RegularExpressionConstant;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "gender", nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private Gender gender = Gender.UNKNOWN;

  @Pattern(regexp = RegularExpressionConstant.DRIVER_PHONE_REGEX)
  @Column(name = "phone", nullable = false)
  private String phone;

  @Builder.Default
  @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Car> cars = new ArrayList<>();

  @Builder.Default
  @Column(name = "is_enabled", nullable = false)
  private Boolean isEnabled = Boolean.TRUE;

}