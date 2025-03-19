package com.vlad.kuzhyr.rideservice.utility.fallback;

import com.vlad.kuzhyr.rideservice.utility.client.DriverFeignClient;
import com.vlad.kuzhyr.rideservice.web.dto.external.DriverResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
@RequiredArgsConstructor
public class DriverFeignClientFallback implements DriverFeignClient {

    private final Throwable cause;

    @Override
    public ResponseEntity<DriverResponse> getDriverById(Long id) {
        log.error("getDriverById: Fallback method called for getDriverById with id: {}", id, cause);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
    }

}
