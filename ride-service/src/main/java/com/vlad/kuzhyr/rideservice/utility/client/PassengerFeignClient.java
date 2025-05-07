package com.vlad.kuzhyr.rideservice.utility.client;

import com.vlad.kuzhyr.rideservice.exception.CustomFeignException;
import com.vlad.kuzhyr.rideservice.exception.PassengerServiceUnavailableException;
import com.vlad.kuzhyr.rideservice.web.dto.external.PassengerResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "${feign.passenger-service.name}",
    path = "${feign.passenger-service.path}",
    configuration = FeignClientInterceptor.class
)
public interface PassengerFeignClient {

    @GetMapping("/{id}")
    @CircuitBreaker(name = "passengerServiceCircuitBreaker", fallbackMethod = "getPassengerByIdFallbackMethod")
    @Retry(name = "passengerServiceRetry", fallbackMethod = "getPassengerByIdFallbackMethod")
    ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id);

    default ResponseEntity<PassengerResponse> getPassengerByIdFallbackMethod(Throwable throwable) {
        if (throwable instanceof CustomFeignException customFeignException) {
            throw customFeignException;
        }

        throw new PassengerServiceUnavailableException(throwable.getMessage());
    }
}
