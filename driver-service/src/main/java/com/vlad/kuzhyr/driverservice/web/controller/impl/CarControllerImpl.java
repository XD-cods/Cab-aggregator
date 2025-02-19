package com.vlad.kuzhyr.driverservice.web.controller.impl;

import com.vlad.kuzhyr.driverservice.service.CarService;
import com.vlad.kuzhyr.driverservice.web.controller.CarController;
import com.vlad.kuzhyr.driverservice.web.dto.request.CarRequest;
import com.vlad.kuzhyr.driverservice.web.dto.response.CarResponse;
import com.vlad.kuzhyr.driverservice.web.dto.response.PageResponse;
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
        CarResponse existingCarResponse = carService.getCarById(id);
        return ResponseEntity.ok(existingCarResponse);
    }

    @Override
    @GetMapping
    public ResponseEntity<PageResponse<CarResponse>> getAllCar(
        @RequestParam(name = "current_page", required = false, defaultValue = "0") @Min(0) Integer currentPage,
        @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(100) Integer limit
    ) {
        PageResponse<CarResponse> carsPage = carService.getAllCar(currentPage, limit);
        return ResponseEntity.ok(carsPage);
    }

    @Override
    @PostMapping
    public ResponseEntity<CarResponse> createCar(@Valid CarRequest carRequest) {
        CarResponse createdCar = carService.createCar(carRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCar);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> updateCar(@PathVariable Long id, @Valid CarRequest carRequest) {
        CarResponse updatedCar = carService.updateCar(id, carRequest);
        return ResponseEntity.ok(updatedCar);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCar(@PathVariable Long id) {
        Boolean isDeleted = carService.deleteCarById(id);
        return ResponseEntity.ok(isDeleted);
    }

}
