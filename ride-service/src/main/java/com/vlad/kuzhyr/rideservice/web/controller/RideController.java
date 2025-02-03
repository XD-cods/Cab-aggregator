package com.vlad.kuzhyr.rideservice.web.controller;

import com.vlad.kuzhyr.rideservice.web.request.RideRequest;
import com.vlad.kuzhyr.rideservice.web.request.UpdateRideRequest;
import com.vlad.kuzhyr.rideservice.web.request.UpdateRideStatusRequest;
import com.vlad.kuzhyr.rideservice.web.response.PageResponse;
import com.vlad.kuzhyr.rideservice.web.response.RideResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Tag(name = "Ride API", description = "API for managing rides data")
public interface RideController {

  @Operation(summary = "Get ride by id")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Ride found"),
          @ApiResponse(responseCode = "404", description = "Ride not found"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<RideResponse> getRideById(@PathVariable Long id);

  @Operation(summary = "Get rides")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Rides found"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<PageResponse<RideResponse>> getAllRides(
          @RequestParam(required = false, defaultValue = "0") @Min(0) Integer offset,
          @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(100) Integer limit
  );

  @Operation(summary = "Get rides by driver id")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Rides found"),
          @ApiResponse(responseCode = "404", description = "Rides not found by driver id"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<PageResponse<RideResponse>> getAllRidesByDriverId(
          @PathVariable Long driverId,
          @RequestParam(required = false, defaultValue = "0") @Min(0) Integer offset,
          @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(100) Integer limit
  );

  @Operation(summary = "Get rides by passenger id")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Rides found"),
          @ApiResponse(responseCode = "404", description = "Rides not found by passenger id"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<PageResponse<RideResponse>> getAllRidesByPassengerId(
          @PathVariable Long passengerId,
          @RequestParam(required = false, defaultValue = "0") @Min(0) Integer offset,
          @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(100) Integer limit
  );

  @Operation(summary = "Create new ride")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Ride created"),
          @ApiResponse(responseCode = "404", description = "Ride not found"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<RideResponse> createRide(@Valid @RequestBody RideRequest rideRequest);

  @Operation(summary = "Update ride by id and ride request")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Ride updated"),
          @ApiResponse(responseCode = "404", description = "Ride not found"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<RideResponse> updateRide(@PathVariable Long id, @Valid @RequestBody UpdateRideRequest rideRequest);

  @Operation(summary = "Update ride status by id and ride status update request")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Ride status updated"),
          @ApiResponse(responseCode = "404", description = "Ride not found"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<RideResponse> updateRideStatus(@PathVariable Long id, @Valid @RequestBody UpdateRideStatusRequest rideRequest);

}
