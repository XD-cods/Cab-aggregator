package com.vlad.kuzhyr.authservice.utility.client;

import com.vlad.kuzhyr.authservice.exception.CustomFeignException;
import com.vlad.kuzhyr.authservice.exception.DriverServiceUnavailableException;
import com.vlad.kuzhyr.authservice.web.dto.external.DriverRequest;
import com.vlad.kuzhyr.authservice.web.dto.external.DriverResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "driver-service",
    path = "/api/v1/drivers"
)
public interface DriverFeignClient {

    @PostMapping
    @CircuitBreaker(name = "driverServiceCircuitBreaker", fallbackMethod = "createDriverFallback")
    @Retry(name = "driverServiceRetry", fallbackMethod = "createDriverFallback")
    ResponseEntity<DriverResponse> createDriver(@Valid @RequestBody DriverRequest driverRequest);

    default ResponseEntity<DriverResponse> createDriverFallback(Throwable throwable) {
        if (throwable instanceof CustomFeignException customFeignException) {
            throw customFeignException;
        }

        throw new DriverServiceUnavailableException(throwable.getMessage());
    }
}
