package com.vlad.kuzhyr.rideservice.persistence.repository;

import com.vlad.kuzhyr.rideservice.persistence.entity.Ride;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
  List<Ride> findByPassengerId(Long passengerId, Pageable pageable);

  List<Ride> findByDriverId(Long driverId, Pageable pageable);

  boolean existsRidesByDriverId(Long driverId);

  boolean existsRidesByPassengerId(Long passengerId);
}
