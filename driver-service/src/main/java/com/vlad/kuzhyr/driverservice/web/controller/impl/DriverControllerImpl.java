package com.vlad.kuzhyr.driverservice.web.controller.impl;

import com.vlad.kuzhyr.driverservice.service.DriverService;
import com.vlad.kuzhyr.driverservice.web.controller.DriverController;
import com.vlad.kuzhyr.driverservice.web.request.DriverRequest;
import com.vlad.kuzhyr.driverservice.web.request.DriverUpdateCarsRequest;
import com.vlad.kuzhyr.driverservice.web.response.DriverResponse;
import com.vlad.kuzhyr.driverservice.web.response.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/drivers")
public class DriverControllerImpl implements DriverController {
  private final DriverService driverService;

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<DriverResponse> getDriverById(@PathVariable Long id) {
    return ResponseEntity.ok(driverService.getDriverById(id));
  }

  @Override
  @GetMapping
  public ResponseEntity<PageResponse<DriverResponse>> getAllDriver(
          @RequestParam(required = false, defaultValue = "0") @Min(0) Integer offset,
          @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(100) Integer limit
  ) {
    return ResponseEntity.ok(driverService.getAllDriver(offset, limit));
  }

  @Override
  @PostMapping
  public ResponseEntity<DriverResponse> createDriver(@Valid DriverRequest driverRequest) {
    return ResponseEntity.status(HttpStatus.CREATED).body(driverService.createDriver(driverRequest));
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<DriverResponse> updateDriver(@PathVariable Long id, @Valid DriverRequest driverRequest) {
    return ResponseEntity.ok(driverService.updateDriver(id, driverRequest));
  }

  @Override
  @PatchMapping("/{id}")
  public ResponseEntity<DriverResponse> updateDriverCarsById(
          @PathVariable Long id,
          @Valid @RequestBody DriverUpdateCarsRequest driverUpdateCarsRequest
  ) {
    return ResponseEntity.ok(driverService.updateDriverCarsById(id, driverUpdateCarsRequest));
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Boolean> deleteDriver(@PathVariable Long id) {
    return ResponseEntity.ok(driverService.deleteDriverById(id));
  }
}
