package com.vlad.kuzhyr.rideservice.constant;

import com.vlad.kuzhyr.rideservice.web.dto.request.RideRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class E2eConstant {

    public static final Long BASE_DRIVER_ID = 1L;
    public static final Long BASE_PASSENGER_ID = 1L;
    public static final String BASE_DEPARTURE_ADDRESS = "улица Московская, 26, Брест, Беларусь";
    public static final String BASE_DESTINATION_ADDRESS = "улица Гагарина, 12, Брест, Беларусь";

    public static RideRequest createRideRequest() {
        return RideRequest.builder()
            .driverId(BASE_DRIVER_ID)
            .passengerId(BASE_PASSENGER_ID)
            .destinationAddress(BASE_DEPARTURE_ADDRESS)
            .departureAddress(BASE_DESTINATION_ADDRESS)
            .build();
    }


}
