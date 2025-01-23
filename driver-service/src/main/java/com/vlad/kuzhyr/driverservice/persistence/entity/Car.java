package com.vlad.kuzhyr.driverservice.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
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
  @Column(name = "id", nullable = false)
  private Long id;

  @NotEmpty(message = "A color can't be empty")
  @Column(nullable = false)
  private String color;

  @NotEmpty(message = "A brand can't be empty")
  @Column(nullable = false)
  private String brand;

  @NotEmpty(message = "A number can't be empty")
  @Column(unique = true, nullable = false)
  private String number;
}