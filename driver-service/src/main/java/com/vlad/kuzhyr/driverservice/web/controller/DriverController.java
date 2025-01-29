package com.vlad.kuzhyr.driverservice.web.controller;

import com.vlad.kuzhyr.driverservice.web.request.DriverRequest;
import com.vlad.kuzhyr.driverservice.web.response.DriverResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Driver API", description = "api for managing drivers")
public interface DriverController {

  @Operation(summary = "Get driver by id")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Driver found"),
          @ApiResponse(responseCode = "404", description = "Driver not found"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<DriverResponse> getDriverById(@PathVariable Long id);

  @Operation(summary = "Get all drivers")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Drivers found"),
          @ApiResponse(responseCode = "404", description = "Drivers not found"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<List<DriverResponse>> getAllDriver(Integer offset, Integer limit);

  @Operation(summary = "Create new driver")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "Driver created"),
          @ApiResponse(responseCode = "409", description = "Driver already exists"),
          @ApiResponse(responseCode = "400", description = "Driver request not a valid"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<DriverResponse> createDriver(@RequestBody
                                              DriverRequest driverRequest);

  @Operation(summary = "Update driver by id and driver request")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Driver updated"),
          @ApiResponse(responseCode = "404", description = "Driver not found"),
          @ApiResponse(responseCode = "400", description = "Driver request not a valid"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<DriverResponse> updateDriver(@PathVariable Long id,
                                              @RequestBody DriverRequest driverRequest);

  @Operation(summary = "Delete driver by id")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Driver deleted by id"),
          @ApiResponse(responseCode = "404", description = "Driver not found by id"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
  })
  ResponseEntity<Boolean> deleteDriver(@PathVariable Long id);

}
