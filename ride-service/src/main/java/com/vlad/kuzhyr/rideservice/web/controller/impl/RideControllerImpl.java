package com.vlad.kuzhyr.rideservice.web.controller.impl;

import com.vlad.kuzhyr.rideservice.service.RideService;
import com.vlad.kuzhyr.rideservice.web.controller.RideController;
import com.vlad.kuzhyr.rideservice.web.request.RideRequest;
import com.vlad.kuzhyr.rideservice.web.request.UpdateRideStatusRequest;
import com.vlad.kuzhyr.rideservice.web.response.RideResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rides")
public class RideControllerImpl implements RideController {
  private final RideService rideService;

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<RideResponse> getRideById(@PathVariable Long id) {
    return ResponseEntity.ok(rideService.getRideById(id));
  }

  @Override
  @GetMapping("/driver/{driverId}")
  public ResponseEntity<List<RideResponse>> getAllRidesByDriverId(
          @PathVariable Long driverId,
          @RequestParam(defaultValue = "0") @Min(0) Integer offset,
          @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit
  ) {
    return ResponseEntity.ok(rideService.getAllRidesByDriverId(driverId, offset, limit));
  }

  @Override
  @GetMapping("/passenger/{passengerId}")
  public ResponseEntity<List<RideResponse>> getAllRidesByPassengerId(
          @PathVariable Long passengerId,
          @RequestParam(defaultValue = "0") @Min(0) Integer offset,
          @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer limit
  ) {
    return ResponseEntity.ok(rideService.getAllRidesByPassengerId(passengerId, offset, limit));
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<RideResponse> updateRide(@PathVariable Long id, @Valid RideRequest rideRequest) {
    return ResponseEntity.ok(rideService.updateRide(id, rideRequest));
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<RideResponse> updateRideStatus(
          @PathVariable Long id,
          @Valid UpdateRideStatusRequest rideRequest
  ) {
    return ResponseEntity.ok(rideService.updateRideStatus(id, rideRequest));
  }

  @Override
  @PostMapping
  public ResponseEntity<RideResponse> createRide(@Valid RideRequest rideRequest) {
    return ResponseEntity.status(HttpStatus.CREATED).body(rideService.createRide(rideRequest));
  }

}
