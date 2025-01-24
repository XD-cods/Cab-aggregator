package com.vlad.kuzhyr.passengerservice.web.controller;

import com.vlad.kuzhyr.passengerservice.service.PassengerService;
import com.vlad.kuzhyr.passengerservice.web.request.PassengerRequest;
import com.vlad.kuzhyr.passengerservice.web.response.PassengerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
@Tag(name = "Passenger API", description = "API for managing passenger data")
public class PassengerController {
  private final PassengerService passengerService;

  @GetMapping("/{id}")
  @Operation(summary = "Get passenger by id")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Passenger found"),
          @ApiResponse(responseCode = "404", description = "Passenger not found"),
          @ApiResponse(responseCode = "400", description = "Passenger not enabled"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<PassengerResponse> getPassengerById(@PathVariable Long id) {
    return ResponseEntity.ok(passengerService.getPassengerById(id));
  }

  @PostMapping
  @Operation(summary = "Create new passenger")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Passenger created"),
          @ApiResponse(responseCode = "404", description = "Passenger not found"),
          @ApiResponse(responseCode = "409", description = "Passenger already exists"),
          @ApiResponse(responseCode = "400", description = "Passenger not enabled"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<PassengerResponse> createPassenger(@ParameterObject @RequestBody PassengerRequest passengerRequest) {
    return ResponseEntity.status(HttpStatus.CREATED).body(passengerService.createPassenger(passengerRequest));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update passenger by id and passenger request")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Passenger updated"),
          @ApiResponse(responseCode = "404", description = "Passenger not found"),
          @ApiResponse(responseCode = "400", description = "Passenger not enabled"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<PassengerResponse> updatePassenger(@PathVariable Long id,
                                                           @RequestBody PassengerRequest passengerRequest) {
    return ResponseEntity.ok(passengerService.updatePassenger(id, passengerRequest));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete passenger by id")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Passenger deleted"),
          @ApiResponse(responseCode = "404", description = "Passenger not found"),
          @ApiResponse(responseCode = "400", description = "Passenger not enabled"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  public ResponseEntity<Boolean> deletePassenger(@PathVariable Long id) {
    return ResponseEntity.ok(passengerService.deletePassengerById(id));
  }

}
