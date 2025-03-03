package com.vlad.kuzhyr.rideservice.constant;

import com.vlad.kuzhyr.rideservice.persistence.entity.Address;
import com.vlad.kuzhyr.rideservice.persistence.entity.KafkaMessage;
import com.vlad.kuzhyr.rideservice.persistence.entity.Ride;
import com.vlad.kuzhyr.rideservice.persistence.entity.RideStatus;
import com.vlad.kuzhyr.rideservice.web.dto.external.DriverResponse;
import com.vlad.kuzhyr.rideservice.web.dto.external.PassengerResponse;
import com.vlad.kuzhyr.rideservice.web.dto.request.RideRequest;
import com.vlad.kuzhyr.rideservice.web.dto.request.UpdateRideRequest;
import com.vlad.kuzhyr.rideservice.web.dto.request.UpdateRideStatusRequest;
import com.vlad.kuzhyr.rideservice.web.dto.response.RideResponse;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class UnitTestDataProvider {

    public static final Long TEST_ID = 1L;
    public static final Long TEST_DRIVER_ID = 2L;
    public static final Long TEST_PASSENGER_ID = 3L;
    private static final String TEST_PASSENGER_EMAIL = "test@gmail.com";
    private static final String TEST_PASSENGER_PHONE = "123456789";
    private static final String TEST_PASSENGER_FIRST_NAME = "John";
    private static final String TEST_PASSENGER_LAST_NAME = "Doe";
    private static final String TEST_DRIVER_EMAIL = "driver@gmail.com";
    private static final String TEST_DRIVER_PHONE = "987654321";
    private static final String TEST_DRIVER_FIRST_NAME = "Mike";
    private static final String TEST_DRIVER_LAST_NAME = "Smith";
    private static final String TEST_GENDER = "MALE";
    private static final BigDecimal TEST_RIDE_PRICE = BigDecimal.valueOf(10);
    private static final Double TEST_RIDE_DISTANCE = 100.0;

    private static final String TEST_ADDRESS1_NAME = "123 Main St, CityA";
    private static final Double TEST_LATITUDE1 = 40.7128;
    private static final Double TEST_LONGITUDE1 = -74.0060;

    private static final String TEST_ADDRESS2_NAME = "456 Elm St, CityB";
    private static final Double TEST_LATITUDE2 = 53.0522;
    private static final Double TEST_LONGITUDE2 = -113.2437;

    private static final String TEST_ADDRESS3_NAME = "318 Min St, CityC";
    private static final Double TEST_LATITUDE3 = 46.2128;
    private static final Double TEST_LONGITUDE3 = -44.3060;

    private static final String TEST_ADDRESS4_NAME = "283 Alb St, CityD";
    private static final Double TEST_LATITUDE4 = 44.0522;
    private static final Double TEST_LONGITUDE4 = -128.2437;

    public static Address address1() {
        return Address.builder()
            .addressName(TEST_ADDRESS1_NAME)
            .latitude(TEST_LATITUDE1)
            .longitude(TEST_LONGITUDE1)
            .build();
    }

    public static Address address2() {
        return Address.builder()
            .addressName(TEST_ADDRESS2_NAME)
            .latitude(TEST_LATITUDE2)
            .longitude(TEST_LONGITUDE2)
            .build();
    }

    public static Address newAddress1() {
        return Address.builder()
            .addressName(TEST_ADDRESS3_NAME)
            .latitude(TEST_LATITUDE3)
            .longitude(TEST_LONGITUDE3)
            .build();
    }

    public static Address newAddress2() {
        return Address.builder()
            .addressName(TEST_ADDRESS4_NAME)
            .latitude(TEST_LATITUDE4)
            .longitude(TEST_LONGITUDE4)
            .build();
    }

    public static RideResponse rideResponse() {
        return RideResponse.builder()
            .id(TEST_ID)
            .driverId(TEST_DRIVER_ID)
            .passengerId(TEST_PASSENGER_ID)
            .departureAddress(address1().getAddressName())
            .destinationAddress(address2().getAddressName())
            .rideStatus(RideStatus.COMPLETED)
            .ridePrice(TEST_RIDE_PRICE)
            .rideDistance(TEST_RIDE_DISTANCE)
            .build();
    }

    public static RideRequest rideRequest() {
        return RideRequest.builder()
            .departureAddress(address1().getAddressName())
            .destinationAddress(address2().getAddressName())
            .passengerId(TEST_PASSENGER_ID)
            .driverId(TEST_DRIVER_ID)
            .build();
    }

    public static UpdateRideRequest updateRideRequest() {
        return UpdateRideRequest.builder()
            .departureAddress(address1().getAddressName())
            .destinationAddress(address2().getAddressName())
            .build();
    }

    public static UpdateRideStatusRequest updateRideStatusRequest() {
        return UpdateRideStatusRequest.builder()
            .rideStatus(RideStatus.COMPLETED)
            .build();
    }

    public static Ride ride() {
        return Ride.builder()
            .id(TEST_ID)
            .driverId(TEST_DRIVER_ID)
            .passengerId(TEST_PASSENGER_ID)
            .departureAddress(address1())
            .destinationAddress(address2())
            .rideDistance(TEST_RIDE_DISTANCE)
            .ridePrice(TEST_RIDE_PRICE)
            .rideStatus(RideStatus.COMPLETED)
            .build();
    }

    public static PassengerResponse passengerResponse() {
        return PassengerResponse.builder()
            .id(TEST_PASSENGER_ID)
            .email(TEST_PASSENGER_EMAIL)
            .phone(TEST_PASSENGER_PHONE)
            .firstName(TEST_PASSENGER_FIRST_NAME)
            .lastName(TEST_PASSENGER_LAST_NAME)
            .isBusy(false)
            .build();
    }

    public static DriverResponse driverResponse() {
        return DriverResponse.builder()
            .id(TEST_DRIVER_ID)
            .carIds(List.of(TEST_ID))
            .lastName(TEST_DRIVER_LAST_NAME)
            .firstName(TEST_DRIVER_FIRST_NAME)
            .email(TEST_DRIVER_EMAIL)
            .phone(TEST_DRIVER_PHONE)
            .gender(TEST_GENDER)
            .isBusy(false)
            .build();
    }

    public static DriverResponse busyDriverResponse() {
        return DriverResponse.builder()
            .id(TEST_DRIVER_ID)
            .carIds(List.of(TEST_ID))
            .lastName(TEST_DRIVER_LAST_NAME)
            .firstName(TEST_DRIVER_FIRST_NAME)
            .email(TEST_DRIVER_EMAIL)
            .phone(TEST_DRIVER_PHONE)
            .gender(TEST_GENDER)
            .isBusy(true)
            .build();
    }

    public static PassengerResponse busyPassengerResponse() {
        return PassengerResponse.builder()
            .id(TEST_PASSENGER_ID)
            .email(TEST_PASSENGER_EMAIL)
            .phone(TEST_PASSENGER_PHONE)
            .firstName(TEST_PASSENGER_FIRST_NAME)
            .lastName(TEST_PASSENGER_LAST_NAME)
            .isBusy(true)
            .build();
    }

    public static DriverResponse driverWithoutCarResponse() {
        return DriverResponse.builder()
            .id(TEST_DRIVER_ID)
            .carIds(Collections.emptyList())
            .lastName(TEST_DRIVER_LAST_NAME)
            .firstName(TEST_DRIVER_FIRST_NAME)
            .email(TEST_DRIVER_EMAIL)
            .phone(TEST_DRIVER_PHONE)
            .gender(TEST_GENDER)
            .isBusy(false)
            .build();
    }

    public static KafkaMessage kafkaMessage() {
        return KafkaMessage.builder()
            .id(1L)
            .topic("test-topic")
            .key(1L)
            .message("test-message")
            .isSent(false)
            .build();
    }
}
