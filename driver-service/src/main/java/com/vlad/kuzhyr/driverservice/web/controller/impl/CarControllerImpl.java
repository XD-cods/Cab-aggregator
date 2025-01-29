package com.vlad.kuzhyr.driverservice.web.controller.impl;

import com.vlad.kuzhyr.driverservice.service.CarService;
import com.vlad.kuzhyr.driverservice.web.controller.CarController;
import com.vlad.kuzhyr.driverservice.web.request.CarRequest;
import com.vlad.kuzhyr.driverservice.web.response.CarResponse;
import com.vlad.kuzhyr.driverservice.web.response.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cars")
public class CarControllerImpl implements CarController {
  private final CarService carService;

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<CarResponse> getCarById(@PathVariable Long id) {
    return ResponseEntity.ok(carService.getCarById(id));
  }

  @Override
  @GetMapping
  public ResponseEntity<PageResponse<CarResponse>> getAllCar(
          @RequestParam(required = false, defaultValue = "0") @Min(0) Integer offset,
          @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(100) Integer limit
  ) {
    return ResponseEntity.ok(carService.getAllCar(offset, limit));
  }

  @Override
  @PostMapping
  public ResponseEntity<CarResponse> createCar(@Valid CarRequest carRequest) {
    return ResponseEntity.status(HttpStatus.CREATED).body(carService.createCar(carRequest));
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<CarResponse> updateCar(@PathVariable Long id, @Valid CarRequest carRequest) {
    return ResponseEntity.ok(carService.updateCar(id, carRequest));
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Boolean> deleteCar(@PathVariable Long id) {
    return ResponseEntity.ok(carService.deleteCarById(id));
  }
}
