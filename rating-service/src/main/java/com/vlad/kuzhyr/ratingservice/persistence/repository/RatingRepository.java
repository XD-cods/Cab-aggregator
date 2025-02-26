package com.vlad.kuzhyr.ratingservice.persistence.repository;

import com.vlad.kuzhyr.ratingservice.persistence.entity.RatedBy;
import com.vlad.kuzhyr.ratingservice.persistence.entity.Rating;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByRideInfo_PassengerIdAndRatedBy(Long passengerId, Pageable pageable, RatedBy ratedBy);

    List<Rating> findByRideInfo_DriverIdAndRatedBy(Long driverId, Pageable pageable, RatedBy ratedBy);

    boolean existsByRideInfo_RideIdAndRatedBy(
        @NotNull(message = "{validation.rating.null}") Long rideInfoId,
        @NotNull(message = "{validation.rated-by.null}") RatedBy ratedBy
    );

}
