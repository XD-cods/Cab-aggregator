package com.vlad.kuzhyr.driverservice.service;

import com.vlad.kuzhyr.driverservice.web.request.DriverRequest;
import com.vlad.kuzhyr.driverservice.web.response.DriverResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Driver API", description = "CRUD Api for managing drivers")
public interface DriverService {

  @Operation(summary = "Get driver by id")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Driver found"),
          @ApiResponse(responseCode = "404", description = "Driver not found"),
          @ApiResponse(responseCode = "400", description = "Driver not a valid"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  DriverResponse getDriverById(Long id);

  @Operation(summary = "Create new driver")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Driver created"),
          @ApiResponse(responseCode = "404", description = "Driver not found"),
          @ApiResponse(responseCode = "409", description = "Driver already exists"),
          @ApiResponse(responseCode = "400", description = "Driver not a valid"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  DriverResponse createDriver(DriverRequest driverRequest);

  @Operation(summary = "Update driver by id and driver request")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Driver updated"),
          @ApiResponse(responseCode = "404", description = "Driver not found"),
          @ApiResponse(responseCode = "400", description = "Driver not a valid"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  DriverResponse updateDriver(Long id, DriverRequest driverRequest);

  @Operation(summary = "Delete driver by id")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Driver deleted"),
          @ApiResponse(responseCode = "404", description = "Driver not found"),
          @ApiResponse(responseCode = "400", description = "Driver not a valid"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  Boolean deleteDriverById(Long id);

  List<DriverResponse> getAllDriver(Integer offset, Integer limit);
}
