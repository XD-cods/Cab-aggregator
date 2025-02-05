package com.vlad.kuzhyr.ratingservice.persistence.entity;

import com.vlad.kuzhyr.ratingservice.utility.mapper.RatedByConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
    @Builder.Default
    private Double rating = 5.0;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "rated_by", nullable = false)
    @Convert(converter = RatedByConverter.class)
    private RatedBy ratedBy;


}
