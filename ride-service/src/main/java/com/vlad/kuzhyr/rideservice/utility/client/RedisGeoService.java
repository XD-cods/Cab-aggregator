package com.vlad.kuzhyr.rideservice.utility.client;

import com.vlad.kuzhyr.rideservice.persistence.entity.Address;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisGeoService {

    private static final String GEO_KEY = "addresses:geo";

    private final StringRedisTemplate stringRedisTemplate;

    public double calculateDistance(Address origin, Address destination) {
        log.debug("calculateDistance: Entering method. Origin: {}, Destination: {}",
            origin.getAddressName(), destination.getAddressName());

        addAddressToGeo(origin);
        addAddressToGeo(destination);

        org.springframework.data.geo.Distance distance = stringRedisTemplate.opsForGeo()
            .distance(GEO_KEY, origin.getAddressName(), destination.getAddressName(),
                RedisGeoCommands.DistanceUnit.METERS);

        if (distance == null) {
            log.error("calculateDistance: Could not calculate distance between '{}' and '{}'",
                origin.getAddressName(), destination.getAddressName());
            throw new RuntimeException("Failed to calculate distance using Redis geospatial");
        }

        double distanceValue = distance.getValue();

        log.debug("calculateDistance: Calculated distance. Origin: {}, Destination: {}, Distance: {} meters",
            origin.getAddressName(), destination.getAddressName(), distanceValue);
        return distanceValue;
    }

    private void addAddressToGeo(Address address) {
        stringRedisTemplate.opsForGeo()
            .add(GEO_KEY, new Point(address.getLongitude(), address.getLatitude()), address.getAddressName());
    }
}
