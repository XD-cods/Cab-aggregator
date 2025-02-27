package com.vlad.kuzhyr.ratingservice.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
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
@Table(name = "ride_info")
public class RideInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ride_info_id", nullable = false)
    private Long rideInfoId;

    @Column(name = "ride_id", nullable = false)
    private Long rideId;

    @Column(name = "driver_id", nullable = false)
    private Long driverId;

    @Column(name = "passenger_id", nullable = false)
    private Long passengerId;

    @OneToMany(mappedBy = "rideInfo", cascade = CascadeType.ALL)
    private List<Rating> rating;

    @Override
    public String toString() {
        return "RideInfo{" +
               "rideInfoId=" + rideInfoId +
               ", rideId=" + rideId +
               ", driverId=" + driverId +
               ", passengerId=" + passengerId +
               '}';
    }
}
