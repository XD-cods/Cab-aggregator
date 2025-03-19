package com.vlad.kuzhyr.rideservice.utility.client;

import com.vlad.kuzhyr.rideservice.utility.fallback.factory.PassengerFeignClientFallbackFactory;
import com.vlad.kuzhyr.rideservice.web.dto.external.PassengerResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "passenger-service",
    path = "/api/v1/passengers",
    fallbackFactory = PassengerFeignClientFallbackFactory.class
)
public interface PassengerFeignClient {

    @GetMapping("/{id}")
    @Retry(name = "passengerServiceRetry")
    @CircuitBreaker(
        name = "passengerServiceCircuitBreaker"
    )
    ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id);

}
