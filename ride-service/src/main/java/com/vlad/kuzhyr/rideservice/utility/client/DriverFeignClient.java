package com.vlad.kuzhyr.rideservice.utility.client;

import com.vlad.kuzhyr.rideservice.utility.fallback.factory.DriverFeignClientFallbackFactory;
import com.vlad.kuzhyr.rideservice.web.dto.external.DriverResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "driver-service",
    path = "/api/v1/drivers",
    fallbackFactory = DriverFeignClientFallbackFactory.class
)
public interface DriverFeignClient {

    @GetMapping("/{id}")
    @Retry(name = "driverServiceRetry")
    @CircuitBreaker(
        name = "driverServiceCircuitBreaker"
    )
    ResponseEntity<DriverResponse> getDriverById(@PathVariable Long id);

}
