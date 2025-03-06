package com.vlad.kuzhyr.passengerservice.constant;

import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import com.vlad.kuzhyr.passengerservice.web.dto.request.PassengerRequest;

public final class IntegrationTestDataProvider {

    public static final String BASE_FIRSTNAME = "Vlad";
    public static final String BASE_LASTNAME = "Kuzhyr";
    public static final String BASE_PHONE = "+375339980129";
    public static final String BASE_EMAIL = "vkuzir7@gmail.com";

    public static Passenger createPassenger() {
        return Passenger.builder()
            .firstName(BASE_FIRSTNAME)
            .lastName(BASE_LASTNAME)
            .phone(BASE_PHONE)
            .email(BASE_EMAIL)
            .build();
    }

    public static PassengerRequest passengerRequest() {
        return PassengerRequest.builder()
            .firstName("Maksim")
            .lastName("Volkov")
            .phone("+375298801179")
            .email("volkov@gmail.com")
            .build();
    }

    public static PassengerRequest updatedPassengerRequest() {
        return PassengerRequest.builder()
            .firstName("UpdatedFirstName")
            .lastName("UpdatedLastName")
            .phone("+375333338238")
            .email("updated@gmail.com")
            .build();
    }

    public static PassengerRequest invalidPassengerRequest() {
        return PassengerRequest.builder()
            .firstName("")
            .lastName("Kuzhyr")
            .phone("+375339980129")
            .email("vkuzir7@gmail.com")
            .build();
    }

}
