package com.vlad.kuzhyr.rideservice.utility.fallback;

import com.vlad.kuzhyr.rideservice.utility.client.PassengerFeignClient;
import com.vlad.kuzhyr.rideservice.web.dto.external.PassengerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
@RequiredArgsConstructor
public class PassengerFeignClientFallback implements PassengerFeignClient {

    private final Throwable cause;

    @Override
    public ResponseEntity<PassengerResponse> getPassengerById(Long id) {
        log.error("getPassengerById: Fallback method called for getPassengerById with id: {}", id, cause);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
    }

}
