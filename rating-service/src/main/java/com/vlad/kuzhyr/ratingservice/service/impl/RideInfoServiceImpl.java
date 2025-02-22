package com.vlad.kuzhyr.ratingservice.service.impl;

import com.vlad.kuzhyr.ratingservice.exception.RideInfoPayloadNotFoundException;
import com.vlad.kuzhyr.ratingservice.persistence.entity.RideInfo;
import com.vlad.kuzhyr.ratingservice.persistence.repository.RideInfoRepository;
import com.vlad.kuzhyr.ratingservice.service.RideInfoService;
import com.vlad.kuzhyr.ratingservice.utility.constant.ExceptionMessageConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RideInfoServiceImpl implements RideInfoService {

    private final RideInfoRepository rideInfoRepository;

    @Override
    public RideInfo getRideInfoByRideId(Long rideId) {
        log.debug("Ride info service. Attempting to find ride info. Ride info id: {}", rideId);

        return rideInfoRepository.findByRideId(rideId)
            .orElseThrow(() -> {
                log.error("Ride info service. Ride info not found. Ride info id: {}", rideId);
                return new RideInfoPayloadNotFoundException(
                    ExceptionMessageConstant.RIDE_INFO_NOT_FOUND_MESSAGE.formatted(rideId)
                );
            });
    }

    @Override
    public void saveRideInfo(RideInfo rideInfo) {
        RideInfo savedRideInfo = rideInfoRepository.save(rideInfo);

        log.debug("Ride info service. Save new ride info. Ride id: {}, passenger id: {}, driver id: {}",
            savedRideInfo.getRideId(),
            savedRideInfo.getPassengerId(),
            savedRideInfo.getDriverId()
        );
    }

}
