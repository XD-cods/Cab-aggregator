package com.vlad.kuzhyr.passengerservice.constant;

import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import com.vlad.kuzhyr.passengerservice.web.dto.request.PassengerRequest;
import com.vlad.kuzhyr.passengerservice.web.dto.response.PassengerResponse;

public class TestDataProvider {

    public static final String TEST_EMAIL = "test@vlad.com";
    public static final String TEST_PHONE = "+375337305271";
    public static final String TEST_FIRST_NAME = "John";
    public static final String TEST_LAST_NAME = "Doe";
    public static final Integer CURRENT_PAGE = 0;
    public static final Integer LIMIT = 20;

    public static Passenger createPassenger() {
        return Passenger.builder()
            .id(1L)
            .phone(TEST_PHONE)
            .email(TEST_EMAIL)
            .firstName(TEST_FIRST_NAME)
            .lastName(TEST_LAST_NAME)
            .isEnabled(true)
            .build();
    }

    public static PassengerRequest createPassengerRequest() {
        return PassengerRequest.builder()
            .phone(TEST_PHONE)
            .email(TEST_EMAIL)
            .firstName(TEST_FIRST_NAME)
            .lastName(TEST_LAST_NAME)
            .build();
    }

    public static PassengerResponse createPassengerResponse() {
        return PassengerResponse.builder()
            .id(1L)
            .phone(TEST_PHONE)
            .email(TEST_EMAIL)
            .firstName(TEST_FIRST_NAME)
            .lastName(TEST_LAST_NAME)
            .build();
    }

}
