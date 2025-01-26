package com.vlad.kuzhyr.driverservice.web.controller;

import com.vlad.kuzhyr.driverservice.web.request.CarRequest;
import com.vlad.kuzhyr.driverservice.web.response.CarResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface CarController {

  @Operation(summary = "Get car by id")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Car found"),
          @ApiResponse(responseCode = "404", description = "Car not found"),
          @ApiResponse(responseCode = "400", description = "Car not enabled"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<CarResponse> getCarById(@PathVariable Long id);

  @Operation(summary = "Create new car")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Car created"),
          @ApiResponse(responseCode = "404", description = "Car not found"),
          @ApiResponse(responseCode = "409", description = "Car already exists"),
          @ApiResponse(responseCode = "400", description = "Car not enabled"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<CarResponse> createCar(@RequestBody
                                        CarRequest carRequest);

  @Operation(summary = "Update car by id and car request")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Car updated"),
          @ApiResponse(responseCode = "404", description = "Car not found"),
          @ApiResponse(responseCode = "400", description = "Car not enabled"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<CarResponse> updateCar(@PathVariable Long id,
                                        @RequestBody CarRequest carRequest);

  @Operation(summary = "Delete car by id")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Car deleted"),
          @ApiResponse(responseCode = "404", description = "Car not found"),
          @ApiResponse(responseCode = "400", description = "Car not enabled"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<Boolean> deleteCar(@PathVariable Long id);

}
