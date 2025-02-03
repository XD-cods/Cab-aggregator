package com.vlad.kuzhyr.rideservice.service;

import com.vlad.kuzhyr.rideservice.web.request.RideRequest;
import com.vlad.kuzhyr.rideservice.web.request.UpdateRideRequest;
import com.vlad.kuzhyr.rideservice.web.request.UpdateRideStatusRequest;
import com.vlad.kuzhyr.rideservice.web.response.PageResponse;
import com.vlad.kuzhyr.rideservice.web.response.RideResponse;

public interface RideService {

  RideResponse getRideById(Long id);

  PageResponse<RideResponse> getAllRidesByDriverId(Long driverId, Integer offset, Integer limit);

  PageResponse<RideResponse> getAllRidesByPassengerId(Long passengerId, Integer offset, Integer limit);

  PageResponse<RideResponse> getAllRides(Integer offset, Integer limit);

  RideResponse updateRide(Long id, UpdateRideRequest rideRequest);

  RideResponse updateRideStatus(Long id, UpdateRideStatusRequest rideRequest);

  RideResponse createRide(RideRequest rideRequest);

}
