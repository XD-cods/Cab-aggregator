package com.vlad.kuzhyr.rideservice.persistence.entity;

import com.vlad.kuzhyr.rideservice.utility.mapper.RideStatusConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_address", nullable = false)
    private Address departureAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_address", nullable = false)
    private Address destinationAddress;

    @Column(name = "distance", nullable = false)
    private Double rideDistance;

    @Column(name = "driver_id", nullable = false)
    private Long driverId;

    @Column(name = "passenger_id", nullable = false)
    private Long passengerId;

    @Builder.Default
    @Convert(converter = RideStatusConverter.class)
    @Column(name = "ride_status", nullable = false)
    private RideStatus rideStatus = RideStatus.CREATED;

    @Column(name = "ride_price", nullable = false)
    private BigDecimal ridePrice;

    @Column(name = "pickup_time")
    private LocalDateTime pickupTime;

    @Column(name = "complete_time")
    private LocalDateTime completeTime;

    @Column(name = "order_create_time")
    @CreationTimestamp
    private LocalDateTime orderCreateTime;
}