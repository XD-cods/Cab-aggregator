package com.vlad.kuzhyr.driverservice.service;

import com.vlad.kuzhyr.driverservice.web.request.CarRequest;
import com.vlad.kuzhyr.driverservice.web.response.CarResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Car API", description = "CRUD Api for managing cars")
public interface CarService {

  @Operation(summary = "Get car by id")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Car found"),
          @ApiResponse(responseCode = "404", description = "Car not found"),
          @ApiResponse(responseCode = "400", description = "Car not a valid"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  CarResponse getCarById(Long id);

  @Operation(summary = "Create new car")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Car created"),
          @ApiResponse(responseCode = "404", description = "Car not found"),
          @ApiResponse(responseCode = "409", description = "Car already exists"),
          @ApiResponse(responseCode = "400", description = "Car not a valid"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  CarResponse createCar(CarRequest carRequest);

  @Operation(summary = "Update car by id and car request")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Car updated"),
          @ApiResponse(responseCode = "404", description = "Car not found"),
          @ApiResponse(responseCode = "400", description = "Car not a valid"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  CarResponse updateCar(Long id, CarRequest carRequest);

  @Operation(summary = "Delete car by id")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Car deleted"),
          @ApiResponse(responseCode = "404", description = "Car not found"),
          @ApiResponse(responseCode = "400", description = "Car not a valid"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  Boolean deleteCarById(Long id);

  List<CarResponse> getAllCar(Integer offset, Integer limit);
}
