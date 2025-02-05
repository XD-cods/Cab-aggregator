package com.vlad.kuzhyr.ratingservice.persistence.repository;

import com.vlad.kuzhyr.ratingservice.persistence.entity.RatedBy;
import com.vlad.kuzhyr.ratingservice.persistence.entity.Rating;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Page<Rating> findAllRatings(Pageable pageable);

    List<Rating> findAllByPassengerId(Long passengerId, Pageable pageable);

    List<Rating> findAllByDriverId(Long driverId, Pageable pageable);

    Optional<Rating> findByRideIdAndRatedBy(Long rideId, RatedBy ratedBy);

}
