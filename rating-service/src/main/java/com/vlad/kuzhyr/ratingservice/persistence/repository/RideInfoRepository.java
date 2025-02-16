package com.vlad.kuzhyr.ratingservice.persistence.repository;

import com.vlad.kuzhyr.ratingservice.persistence.entity.RideInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideInfoRepository extends JpaRepository<RideInfo, Long> {

    Optional<RideInfo> findByRideId(Long rideId);

}
