package com.vlad.kuzhyr.authservice.utility.client;

import com.vlad.kuzhyr.authservice.exception.CustomFeignException;
import com.vlad.kuzhyr.authservice.exception.PassengerServiceUnavailableException;
import com.vlad.kuzhyr.authservice.web.dto.external.PassengerRequest;
import com.vlad.kuzhyr.authservice.web.dto.external.PassengerResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "${feign.passenger-service.name}",
    path = "${feign.passenger-service.path}"
)
public interface PassengerFeignClient {

    @PostMapping
    @CircuitBreaker(name = "passengerServiceCircuitBreaker", fallbackMethod = "createPassengerFallback")
    @Retry(name = "passengerServiceRetry", fallbackMethod = "createPassengerFallback")
    ResponseEntity<PassengerResponse> createPassenger(@Valid @RequestBody PassengerRequest passengerRequest);

    default ResponseEntity<PassengerResponse> createPassengerFallback(Throwable throwable) {
        if (throwable instanceof CustomFeignException customFeignException) {
            throw customFeignException;
        }

        throw new PassengerServiceUnavailableException(throwable.getMessage());
    }
}
