package com.vlad.kuzhyr.passengerservice.web.controller.impl;

import com.vlad.kuzhyr.passengerservice.service.PassengerService;
import com.vlad.kuzhyr.passengerservice.web.controller.PassengerController;
import com.vlad.kuzhyr.passengerservice.web.dto.request.PassengerRequest;
import com.vlad.kuzhyr.passengerservice.web.dto.response.PageResponse;
import com.vlad.kuzhyr.passengerservice.web.dto.response.PassengerResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
@Validated
public class PassengerControllerImpl implements PassengerController {

    private final PassengerService passengerService;

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id) {
        return ResponseEntity.ok(passengerService.getPassengerById(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<PageResponse<PassengerResponse>> getPassengers(
        @RequestParam(name = "current_page", required = false, defaultValue = "0") @Min(0) Integer currentPage,
        @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(100) Integer limit
    ) {
        return ResponseEntity.ok(passengerService.getPassengers(currentPage, limit));
    }

    @PostMapping
    @Override
    public ResponseEntity<PassengerResponse> createPassenger(@Valid @RequestBody PassengerRequest passengerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(passengerService.createPassenger(passengerRequest));
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<PassengerResponse> updatePassenger(
        @PathVariable Long id,
        @Valid @RequestBody PassengerRequest passengerRequest
    ) {
        return ResponseEntity.ok(passengerService.updatePassenger(id, passengerRequest));
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Boolean> deletePassenger(@PathVariable Long id) {
        return ResponseEntity.ok(passengerService.deletePassengerById(id));
    }

}
