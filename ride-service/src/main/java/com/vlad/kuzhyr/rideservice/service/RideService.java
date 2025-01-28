package com.vlad.kuzhyr.rideservice.service;

import com.vlad.kuzhyr.rideservice.web.request.RideRequest;
import com.vlad.kuzhyr.rideservice.web.request.UpdateRideStatusRequest;
import com.vlad.kuzhyr.rideservice.web.response.RideResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface RideService {

  RideResponse getRideById(Long id);

  RideResponse createRide(@Valid RideRequest rideRequest);

  RideResponse updateRide(Long id, @Valid RideRequest rideRequest);

  List<RideResponse> getAllRidesByDriverId(Long driverId, Integer offset, Integer limit);

  List<RideResponse> getAllRidesByPassengerId(Long passengerId, Integer offset, Integer limit);

  RideResponse updateRideStatus(Long id, @Valid UpdateRideStatusRequest rideRequest);
}
