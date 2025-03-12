package com.vlad.kuzhyr.rideservice.utility.client;

import com.vlad.kuzhyr.rideservice.web.dto.external.DriverResponse;
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
    ResponseEntity<DriverResponse> getDriverById(@PathVariable Long id);

}
