package com.vlad.kuzhyr.rideservice.constant;

import com.vlad.kuzhyr.rideservice.persistence.entity.RideStatus;
import com.vlad.kuzhyr.rideservice.web.dto.request.RideRequest;
import com.vlad.kuzhyr.rideservice.web.dto.request.UpdateRideRequest;
import com.vlad.kuzhyr.rideservice.web.dto.request.UpdateRideStatusRequest;

public final class IntegrationTestDataProvider {

    public static final Long BASE_DRIVER_ID = 1L;
    public static final Long BASE_PASSENGER_ID = 1L;
    public static final String BASE_DEPARTURE_ADDRESS = "улица Жукова, 26, Брест, Беларусь";
    public static final String BASE_DESTINATION_ADDRESS = "улица Орджоникидзе, 12, Брест, Беларусь";
    public static final String BASE_DEPARTURE_ADDRESS2 = "Долгобродская улица, 29к64, Минск, Беларусь";
    public static final String BASE_DESTINATION_ADDRESS2 = "проспект Рокоссовского, 33, Минск, Беларусь";

    public static RideRequest createRideRequest() {
        return RideRequest.builder()
            .driverId(BASE_DRIVER_ID)
            .passengerId(BASE_PASSENGER_ID)
            .destinationAddress(BASE_DEPARTURE_ADDRESS)
            .departureAddress(BASE_DESTINATION_ADDRESS)
            .build();
    }

    public static UpdateRideRequest createUpdateRideRequest() {
        return UpdateRideRequest.builder()
            .departureAddress("улица Воровского, 20, Брест, Беларусь")
            .destinationAddress("проспект Машерова, 67, Брест, Беларусь")
            .build();
    }

    public static UpdateRideStatusRequest createUpdateRideStatusRequest() {
        return UpdateRideStatusRequest.builder()
            .rideStatus(RideStatus.WAITING_FOR_DRIVER)
            .build();
    }

    public static RideRequest createRideRequest2() {
        return RideRequest.builder()
            .departureAddress(BASE_DEPARTURE_ADDRESS2)
            .destinationAddress(BASE_DESTINATION_ADDRESS2)
            .passengerId(BASE_PASSENGER_ID)
            .driverId(BASE_DRIVER_ID)
            .build();
    }
}