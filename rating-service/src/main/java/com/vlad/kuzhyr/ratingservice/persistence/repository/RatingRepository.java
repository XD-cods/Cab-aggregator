package com.vlad.kuzhyr.ratingservice.persistence.repository;

import com.vlad.kuzhyr.ratingservice.persistence.entity.RatedBy;
import com.vlad.kuzhyr.ratingservice.persistence.entity.Rating;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByRideInfo_PassengerIdAndRatedBy(Long passengerId, Pageable pageable, RatedBy ratedBy);

    List<Rating> findByRideInfo_DriverIdAndRatedBy(Long driverId, Pageable pageable, RatedBy ratedBy);

    Optional<Rating> findByRideInfo_RideIdAndRatedBy(Long rideId, RatedBy ratedBy);

}