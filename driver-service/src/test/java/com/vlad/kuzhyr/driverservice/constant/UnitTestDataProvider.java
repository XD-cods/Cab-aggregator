package com.vlad.kuzhyr.driverservice.constant;

import com.vlad.kuzhyr.driverservice.persistence.entity.Car;
import com.vlad.kuzhyr.driverservice.persistence.entity.Driver;
import com.vlad.kuzhyr.driverservice.persistence.entity.Gender;
import com.vlad.kuzhyr.driverservice.web.dto.request.CarRequest;
import com.vlad.kuzhyr.driverservice.web.dto.request.DriverRequest;
import com.vlad.kuzhyr.driverservice.web.dto.request.DriverUpdateCarsRequest;
import com.vlad.kuzhyr.driverservice.web.dto.response.CarResponse;
import com.vlad.kuzhyr.driverservice.web.dto.response.DriverResponse;
import java.util.List;

public class UnitTestDataProvider {

    public static final Long TEST_ID = 1L;
    public static final String TEST_DRIVER_EMAIL = "test@vlad.com";
    public static final String TEST_DRIVER_PHONE = "+375337305271";
    public static final String TEST_DRIVER_FIRST_NAME = "John";
    public static final String TEST_DRIVER_LAST_NAME = "Doe";
    public static final String TEST_CAR_NUMBER = "8682AX-3";
    private static final String TEST_CAR_BRAND = "Mercedes";
    private static final String TEST_CAR_COLOR = "red";

    public static Car car() {
        return Car.builder()
            .id(TEST_ID)
            .carBrand(TEST_CAR_BRAND)
            .carNumber(TEST_CAR_NUMBER)
            .color(TEST_CAR_COLOR)
            .build();
    }

    public static CarResponse carResponse() {
        return CarResponse.builder()
            .id(TEST_ID)
            .carBrand(TEST_CAR_BRAND)
            .carNumber(TEST_CAR_NUMBER)
            .color(TEST_CAR_COLOR)
            .build();
    }

    public static CarRequest carCreateRequest() {
        return CarRequest.builder()
            .carBrand(TEST_CAR_BRAND)
            .carNumber(TEST_CAR_NUMBER)
            .color(TEST_CAR_COLOR)
            .build();
    }

    public static CarResponse carCreateResponse() {
        return CarResponse.builder()
            .id(TEST_ID)
            .carBrand(TEST_CAR_BRAND)
            .carNumber(TEST_CAR_NUMBER)
            .color(TEST_CAR_COLOR)
            .build();
    }

    public static CarRequest carUpdateRequest() {
        return CarRequest.builder()
            .carBrand("Tesla")
            .carNumber("AB9704-7")
            .color("black")
            .build();
    }

    public static CarResponse carUpdateResponse() {
        return CarResponse.builder()
            .id(TEST_ID)
            .carBrand("Tesla")
            .carNumber("AB9704-7")
            .color("black")
            .build();
    }

    public static Driver driver() {
        return Driver.builder()
            .id(TEST_ID)
            .firstName(TEST_DRIVER_FIRST_NAME)
            .lastName(TEST_DRIVER_LAST_NAME)
            .phone(TEST_DRIVER_PHONE)
            .gender(Gender.MALE)
            .email(TEST_DRIVER_EMAIL)
            .build();
    }

    public static DriverResponse driverResponse() {
        return DriverResponse.builder()
            .id(TEST_ID)
            .firstName(TEST_DRIVER_FIRST_NAME)
            .lastName(TEST_DRIVER_LAST_NAME)
            .phone(TEST_DRIVER_PHONE)
            .gender(Gender.MALE.toString())
            .email(TEST_DRIVER_EMAIL)
            .build();
    }

    public static DriverRequest driverCreateRequest() {
        return DriverRequest.builder()
            .firstName(TEST_DRIVER_FIRST_NAME)
            .lastName(TEST_DRIVER_LAST_NAME)
            .phone(TEST_DRIVER_PHONE)
            .email(TEST_DRIVER_EMAIL)
            .gender(Gender.MALE)
            .carIds(List.of(TEST_ID))
            .build();
    }

    public static DriverResponse driverCreateResponse() {
        return DriverResponse.builder()
            .id(TEST_ID)
            .firstName(TEST_DRIVER_FIRST_NAME)
            .lastName(TEST_DRIVER_LAST_NAME)
            .gender(Gender.MALE.toString())
            .phone(TEST_DRIVER_PHONE)
            .email(TEST_DRIVER_EMAIL)
            .build();
    }

    public static DriverRequest driverUpdateRequest() {
        return DriverRequest.builder()
            .firstName("Jane")
            .lastName("Smith")
            .gender(Gender.FEMALE)
            .phone("+375447305271")
            .email("jane.smith@vlad.com")
            .carIds(List.of(TEST_ID))
            .build();
    }

    public static DriverResponse driverUpdateResponse() {
        return DriverResponse.builder()
            .id(TEST_ID)
            .firstName("Jane")
            .lastName("Smith")
            .gender(Gender.FEMALE.toString())
            .phone("+375447305271")
            .email("jane.smith@vlad.com")
            .build();
    }

    public static DriverUpdateCarsRequest driverUpdateCarsRequest() {
        return DriverUpdateCarsRequest.builder()
            .carIds(List.of(TEST_ID))
            .build();
    }

    public static DriverResponse driverUpdateCarsResponse() {
        return DriverResponse.builder()
            .id(TEST_ID)
            .firstName(TEST_DRIVER_FIRST_NAME)
            .lastName(TEST_DRIVER_LAST_NAME)
            .phone(TEST_DRIVER_PHONE)
            .email(TEST_DRIVER_EMAIL)
            .gender(Gender.MALE.toString())
            .carIds(List.of(TEST_ID))
            .build();
    }

    public static CarRequest carInvalidRequest() {
        return CarRequest.builder().build();
    }

    public static DriverRequest driverInvalidRequest() {
        return DriverRequest.builder().build();
    }

}
