package com.vlad.kuzhyr.ratingservice.service;

import com.vlad.kuzhyr.ratingservice.persistence.entity.RideInfo;

public interface RideInfoService {

    RideInfo getRideInfoByRideId(Long rideId);

    void saveRideInfo(RideInfo rideInfo);

}
