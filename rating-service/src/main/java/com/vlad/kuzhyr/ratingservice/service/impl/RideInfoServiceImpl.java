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
        log.debug("getRideInfoByRideId: Entering method. Ride id: {}", rideId);

        RideInfo rideInfo = rideInfoRepository.findByRideId(rideId)
            .orElseThrow(() -> {
                log.error("getRideInfoByRideId: Ride info not found. Ride id: {}", rideId);
                return new RideInfoPayloadNotFoundException(
                    ExceptionMessageConstant.RIDE_INFO_NOT_FOUND_MESSAGE.formatted(rideId)
                );
            });

        log.info("getRideInfoByRideId: Ride info found. Ride id: {}", rideId);
        return rideInfo;
    }

    @Override
    public void saveRideInfo(RideInfo rideInfo) {
        log.debug("saveRideInfo: Entering method. Ride info: {}", rideInfo);

        RideInfo savedRideInfo = rideInfoRepository.save(rideInfo);

        log.info("saveRideInfo: Ride info saved successfully. Ride info: {}", savedRideInfo);
    }
}
