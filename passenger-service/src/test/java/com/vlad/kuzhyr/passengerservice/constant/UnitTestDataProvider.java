package com.vlad.kuzhyr.passengerservice.constant;

import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import com.vlad.kuzhyr.passengerservice.web.dto.request.PassengerRequest;
import com.vlad.kuzhyr.passengerservice.web.dto.response.PassengerResponse;

public class UnitTestDataProvider {

    public static final Long TEST_PASSENGER_ID = 1L;
    public static final String TEST_PASSENGER_EMAIL = "test@vlad.com";
    public static final String TEST_PASSENGER_PHONE = "+375337305271";
    public static final String TEST_PASSENGER_FIRST_NAME = "John";
    public static final String TEST_PASSENGER_LAST_NAME = "Doe";

    public static Passenger passenger() {
        return Passenger.builder()
            .id(TEST_PASSENGER_ID)
            .phone(TEST_PASSENGER_PHONE)
            .email(TEST_PASSENGER_EMAIL)
            .firstName(TEST_PASSENGER_FIRST_NAME)
            .lastName(TEST_PASSENGER_LAST_NAME)
            .isEnabled(true)
            .build();
    }

    public static PassengerRequest passengerCreateRequest() {
        return PassengerRequest.builder()
            .phone(TEST_PASSENGER_PHONE)
            .email(TEST_PASSENGER_EMAIL)
            .firstName(TEST_PASSENGER_FIRST_NAME)
            .lastName(TEST_PASSENGER_LAST_NAME)
            .build();
    }

    public static PassengerRequest passengerUpdateRequest() {
        return PassengerRequest.builder()
            .phone("+375337305272")
            .email("updated@vlad.com")
            .firstName("Jane")
            .lastName("Smith")
            .build();
    }

    public static PassengerRequest passengerInvalidRequest() {
        return PassengerRequest.builder()
            .phone("invalid")
            .email("invalid")
            .firstName("")
            .lastName("")
            .build();
    }

    public static PassengerResponse passengerResponse() {
        return PassengerResponse.builder()
            .id(TEST_PASSENGER_ID)
            .phone(TEST_PASSENGER_PHONE)
            .email(TEST_PASSENGER_EMAIL)
            .firstName(TEST_PASSENGER_FIRST_NAME)
            .lastName(TEST_PASSENGER_LAST_NAME)
            .isEnabled(true)
            .build();
    }

    public static PassengerResponse passengerCreateResponse() {
        return PassengerResponse.builder()
            .id(TEST_PASSENGER_ID)
            .phone(TEST_PASSENGER_PHONE)
            .email(TEST_PASSENGER_EMAIL)
            .firstName(TEST_PASSENGER_FIRST_NAME)
            .lastName(TEST_PASSENGER_LAST_NAME)
            .isEnabled(true)
            .build();
    }

    public static PassengerResponse passengerUpdateResponse() {
        return PassengerResponse.builder()
            .id(TEST_PASSENGER_ID)
            .phone("+375337305272")
            .email("updated@vlad.com")
            .firstName("Jane")
            .lastName("Smith")
            .build();
    }

    public static Passenger passengerDisabled() {
        return Passenger.builder()
            .id(TEST_PASSENGER_ID)
            .phone(TEST_PASSENGER_PHONE)
            .email(TEST_PASSENGER_EMAIL)
            .firstName(TEST_PASSENGER_FIRST_NAME)
            .lastName(TEST_PASSENGER_LAST_NAME)
            .isEnabled(false)
            .build();
    }
}
