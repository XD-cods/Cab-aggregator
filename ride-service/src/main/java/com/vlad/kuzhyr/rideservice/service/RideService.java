package com.vlad.kuzhyr.rideservice.service;

import com.vlad.kuzhyr.rideservice.web.dto.request.DriverAssignRequest;
import com.vlad.kuzhyr.rideservice.web.dto.request.RideRequest;
import com.vlad.kuzhyr.rideservice.web.dto.request.UpdateRideRequest;
import com.vlad.kuzhyr.rideservice.web.dto.request.UpdateRideStatusRequest;
import com.vlad.kuzhyr.rideservice.web.dto.response.PageResponse;
import com.vlad.kuzhyr.rideservice.web.dto.response.RideResponse;
import jakarta.validation.Valid;

public interface RideService {

    RideResponse getRideById(Long id);

    PageResponse<RideResponse> getAllRidesByDriverId(Long driverId, Integer currentPage, Integer limit);

    PageResponse<RideResponse> getAllRidesByPassengerId(Long passengerId, Integer currentPage, Integer limit);

    PageResponse<RideResponse> getAllRides(Integer currentPage, Integer limit);

    RideResponse updateRide(Long id, UpdateRideRequest rideRequest);

    RideResponse updateRideStatus(Long id, UpdateRideStatusRequest rideRequest);

    RideResponse createRide(RideRequest rideRequest);

    RideResponse assignDriver(@Valid DriverAssignRequest driverAssignRequest, Long rideId);
}
