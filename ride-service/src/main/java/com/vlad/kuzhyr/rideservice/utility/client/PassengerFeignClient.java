package com.vlad.kuzhyr.rideservice.utility.client;

import com.vlad.kuzhyr.rideservice.web.dto.external.PassengerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "passenger-service",
    url = "${routes.passenger-service}"
)
public interface PassengerFeignClient {

    @GetMapping("/{id}")
    ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id);

}
