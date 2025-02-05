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

    List<Rating> findByPassengerIdAndRatedBy(Long passengerId, Pageable pageable, RatedBy ratedBy);

    List<Rating> findByDriverIdAndRatedBy(Long driverId, Pageable pageable, RatedBy ratedBy);

    Optional<Rating> findByRideIdAndRatedBy(Long rideId, RatedBy ratedBy);

}