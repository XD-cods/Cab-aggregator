package com.vlad.kuzhyr.passengerservice.web.controller.impl;

import com.vlad.kuzhyr.passengerservice.service.PassengerService;
import com.vlad.kuzhyr.passengerservice.web.controller.PassengerController;
import com.vlad.kuzhyr.passengerservice.web.request.PassengerRequest;
import com.vlad.kuzhyr.passengerservice.web.response.PassengerResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/passengers")
@RequiredArgsConstructor
public class PassengerControllerImpl implements PassengerController {
  private final PassengerService passengerService;

  @GetMapping("/{id}")
  @Override
  public ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id) {
    return ResponseEntity.ok(passengerService.getPassengerById(id));
  }

  @PostMapping
  @Override
  public ResponseEntity<PassengerResponse> createPassenger(@Valid @RequestBody PassengerRequest passengerRequest) {
    return ResponseEntity.status(HttpStatus.CREATED).body(passengerService.createPassenger(passengerRequest));
  }

  @PutMapping("/{id}")
  @Override
  public ResponseEntity<PassengerResponse> updatePassenger(@PathVariable Long id,
                                                           @Valid @RequestBody PassengerRequest passengerRequest) {
    return ResponseEntity.ok(passengerService.updatePassenger(id, passengerRequest));
  }

  @DeleteMapping("/{id}")
  @Override
  public ResponseEntity<Boolean> deletePassenger(@PathVariable Long id) {
    return ResponseEntity.ok(passengerService.deletePassengerById(id));
  }

}
