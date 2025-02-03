package com.vlad.kuzhyr.rideservice.persistence.repository;

import com.vlad.kuzhyr.rideservice.persistence.entity.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

  Page<Ride> findByPassengerId(Long passengerId, Pageable pageable);

  Page<Ride> findByDriverId(Long driverId, Pageable pageable);

  boolean existsRidesByDriverId(Long driverId);

  boolean existsRidesByPassengerId(Long passengerId);

}
