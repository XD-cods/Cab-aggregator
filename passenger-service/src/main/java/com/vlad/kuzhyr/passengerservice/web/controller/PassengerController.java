package com.vlad.kuzhyr.passengerservice.web.controller;

import com.vlad.kuzhyr.passengerservice.services.PassengerService;
import com.vlad.kuzhyr.passengerservice.web.request.PassengerRequest;
import com.vlad.kuzhyr.passengerservice.web.response.PassengerResponse;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1/passenger")
@RequiredArgsConstructor
public class PassengerController {
  private final PassengerService passengerService;

  @GetMapping("/{id}")
  public ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id) {
    return ResponseEntity.ok(passengerService.getPassengerById(id));
  }

  @PostMapping
  public ResponseEntity<PassengerResponse> createPassenger(@RequestBody PassengerRequest passengerRequest) {
    return ResponseEntity.ok(passengerService.createPassenger(passengerRequest));
  }

  @PutMapping("/{id}")
  public ResponseEntity<PassengerResponse> updatePassenger(@PathVariable Long id,
                                                           @RequestBody PassengerRequest passengerRequest) {
    return ResponseEntity.ok(passengerService.updatePassenger(id, passengerRequest));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Boolean> deletePassenger(@PathVariable Long id) {
    return ResponseEntity.ok(passengerService.deletePassengerById(id));
  }
}
