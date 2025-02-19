package com.vlad.kuzhyr.passengerservice.unittest;

import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import com.vlad.kuzhyr.passengerservice.web.dto.request.PassengerRequest;
import com.vlad.kuzhyr.passengerservice.web.dto.response.PassengerResponse;

public class TestDataProvider {

    public static Passenger createPassenger() {
        return Passenger.builder()
            .id(1L)
            .phone("1234567890")
            .email("test@example.com")
            .firstName("John")
            .lastName("Doe")
            .isEnabled(true)
            .build();
    }

    public static PassengerRequest createPassengerRequest() {
        return PassengerRequest.builder()
            .phone("1234567890")
            .email("test@example.com")
            .firstName("John")
            .lastName("Doe")
            .build();
    }

    public static PassengerResponse createPassengerResponse() {
        return PassengerResponse.builder()
            .id(1L)
            .phone("1234567890")
            .email("test@example.com")
            .firstName("John")
            .lastName("Doe")
            .build();
    }

}
