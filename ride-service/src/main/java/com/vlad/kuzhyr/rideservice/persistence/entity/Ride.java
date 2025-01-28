package com.vlad.kuzhyr.rideservice.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ride")
public class Ride {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ride_id", nullable = false)
  private Long id;

  @Column(name = "start_address", nullable = false)
  private String startAddress;

  @Column(name = "finish_address", nullable = false)
  private String finishAddress;

  @Column(name = "driver_id", nullable = false)
  private Long driverId;

  @Column(name = "passenger_id", nullable = false)
  private Long passengerId;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  @Column(name = "ride_status", nullable = false)
  private RideStatus rideStatus = RideStatus.CREATED;

  @Column(name = "ride_price", nullable = false)
  private BigDecimal ridePrice;

  @Column(name = "start_time")
  private LocalDateTime startTime;

  @Column(name = "finish_time")
  private LocalDateTime finishTime;

}