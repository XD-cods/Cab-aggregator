package com.vlad.kuzhyr.rideservice.utility.client;

import com.vlad.kuzhyr.rideservice.exception.CustomFeignException;
import com.vlad.kuzhyr.rideservice.exception.DriverServiceUnavailableException;
import com.vlad.kuzhyr.rideservice.web.dto.external.DriverResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "driver-service",
    path = "/api/v1/drivers"
)
public interface DriverFeignClient {

    @GetMapping("/{id}")
    @CircuitBreaker(name = "driverServiceCircuitBreaker", fallbackMethod = "getDriverByIdFallbackMethod")
    @Retry(name = "driverServiceRetry", fallbackMethod = "getDriverByIdFallbackMethod")
    ResponseEntity<DriverResponse> getDriverById(@PathVariable Long id);

    default ResponseEntity<DriverResponse> getDriverByIdFallbackMethod(Throwable throwable) {
        if (throwable instanceof CustomFeignException customFeignException) {
            throw customFeignException;
        }

        throw new DriverServiceUnavailableException(throwable.getMessage());
    }
}
