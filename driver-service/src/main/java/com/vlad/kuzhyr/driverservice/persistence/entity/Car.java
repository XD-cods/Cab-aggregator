package com.vlad.kuzhyr.driverservice.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "car")
public class Car {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "car_id", nullable = false)
  private Long id;

  @Column(name = "color", nullable = false)
  private String color;

  @Column(name = "car_brand", nullable = false)
  private String carBrand;

  @Column(name = "car_number", unique = true, nullable = false)
  private String carNumber;

  @Builder.Default
  @Column(name = "is_enabled", nullable = false)
  private Boolean isEnabled = Boolean.TRUE;

}