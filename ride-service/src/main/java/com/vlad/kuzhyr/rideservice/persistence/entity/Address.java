package com.vlad.kuzhyr.rideservice.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
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
@Table(name = "address")
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false)
    private Long id;

    @Column(name = "address_name", nullable = false)
    private String addressName;

    @Column(name = "address_latitude", nullable = false)
    private Double latitude;

    @Column(name = "address_longitude", nullable = false)
    private Double longitude;

    @Override
    public String toString() {
        return "{" +
               "latitude=" + latitude +
               ", longitude=" + longitude +
               '}';
    }
}
