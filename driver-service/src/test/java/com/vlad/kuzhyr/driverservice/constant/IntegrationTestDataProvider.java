package com.vlad.kuzhyr.driverservice.constant;

import com.vlad.kuzhyr.driverservice.persistence.entity.Car;
import com.vlad.kuzhyr.driverservice.persistence.entity.Driver;
import com.vlad.kuzhyr.driverservice.persistence.entity.Gender;
import com.vlad.kuzhyr.driverservice.web.dto.request.CarRequest;
import com.vlad.kuzhyr.driverservice.web.dto.request.DriverRequest;
import java.util.List;

public final class IntegrationTestDataProvider {

    public static final String BASE_FIRSTNAME = "Vlad";
    public static final String BASE_LASTNAME = "Kuzhyr";
    public static final String BASE_PHONE = "+375339980129";
    public static final String BASE_EMAIL = "vkuzir7@gmail.com";

    public static final String BASE_COLOR = "red";
    public static final String BASE_BRAND = "Mercedes";
    public static final String BASE_CAR_NUMBER = "9X92GH";

    public static Driver createDriver() {
        return Driver.builder()
            .firstName(BASE_FIRSTNAME)
            .lastName(BASE_LASTNAME)
            .phone(BASE_PHONE)
            .email(BASE_EMAIL)
            .build();
    }

    public static Car createCar() {
        return Car.builder()
            .color(BASE_COLOR)
            .carBrand(BASE_BRAND)
            .carNumber(BASE_CAR_NUMBER)
            .build();
    }

    public static DriverRequest driverRequest() {
        return DriverRequest.builder()
            .firstName("Maksim")
            .lastName("Volkov")
            .phone("+375298801179")
            .email("volkov@gmail.com")
            .gender(Gender.MALE)
            .carIds(List.of())
            .build();
    }

    public static DriverRequest updatedDriverRequest() {
        return DriverRequest.builder()
            .firstName("UpdatedFirstName")
            .lastName("UpdatedLastName")
            .phone("+375333338238")
            .email("updated@gmail.com")
            .gender(Gender.MALE)
            .carIds(List.of())
            .build();
    }

    public static DriverRequest invalidDriverRequest() {
        return DriverRequest.builder()
            .firstName("")
            .lastName("")
            .phone("123456789")
            .email("ivan@gmail.com")
            .gender(Gender.MALE)
            .carIds(List.of())
            .build();
    }

    public static CarRequest carRequest() {
        return CarRequest.builder()
            .color("green")
            .carBrand("Tesla")
            .carNumber("7X923OK")
            .build();
    }

    public static CarRequest updatedCarRequest() {
        return CarRequest.builder()
            .color("UpdatedCarColor")
            .carBrand("UpdatedCarBrand")
            .carNumber("9L999KL")
            .build();
    }

    public static CarRequest invalidCarRequest() {
        return CarRequest.builder()
            .color("yellow")
            .carBrand("")
            .carNumber("")
            .build();
    }

}
