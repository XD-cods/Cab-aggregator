package com.vlad.kuzhyr.ratingservice.persistence.entity;

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
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rating")
public class Rating {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "rating_id", nullable = false)
  private Long id;

  @Column(name = "driver_id", nullable = false)
  private Long driverId;

  @Column(name = "ride_id", nullable = false)
  private Long rideId;

  @Column(name = "passenger_id", nullable = false)
  private Long passengerId;

  @Column(name = "rating", nullable = false)
  private Float rating = 5.0F;

  @Column(name = "comment", nullable = false)
  private String comment;
}
