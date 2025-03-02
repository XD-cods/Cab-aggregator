package com.vlad.kuzhyr.driverservice.web.controller;

import com.vlad.kuzhyr.driverservice.web.dto.request.CarRequest;
import com.vlad.kuzhyr.driverservice.web.dto.response.CarResponse;
import com.vlad.kuzhyr.driverservice.web.dto.response.PageResponse;
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

@Tag(name = "Car API", description = "api for managing cars")
public interface CarController {

    @Operation(summary = "Get car by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Car found"),
        @ApiResponse(responseCode = "404", description = "Car not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<CarResponse> getCarById(@PathVariable Long id);

    @Operation(summary = "Get all cars")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cars found"),
        @ApiResponse(responseCode = "404", description = "Cars not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<PageResponse<CarResponse>> getAllCar(
        @RequestParam(name = "current_page", required = false, defaultValue = "0") @Min(0) Integer currentPage,
        @RequestParam(required = false, defaultValue = "10") @Min(1) @Max(100) Integer limit
    );

    @Operation(summary = "Create new car")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Car created"),
        @ApiResponse(responseCode = "404", description = "Car not found"),
        @ApiResponse(responseCode = "409", description = "Car already exists"),
        @ApiResponse(responseCode = "400", description = "Car request not a valid"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<CarResponse> createCar(@Valid @RequestBody CarRequest carRequest);

    @Operation(summary = "Update car by id and car request")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Car updated"),
        @ApiResponse(responseCode = "404", description = "Car not found"),
        @ApiResponse(responseCode = "400", description = "Car request not a valid"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<CarResponse> updateCar(
        @PathVariable Long id,
        @Valid @RequestBody CarRequest carRequest
    );

    @Operation(summary = "Delete car by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Car deleted"),
        @ApiResponse(responseCode = "404", description = "Car not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<Boolean> deleteCar(@PathVariable Long id);

}
