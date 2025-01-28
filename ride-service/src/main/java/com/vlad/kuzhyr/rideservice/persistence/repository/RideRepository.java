package com.vlad.kuzhyr.rideservice.persistence.repository;

import com.vlad.kuzhyr.rideservice.persistence.entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
}
