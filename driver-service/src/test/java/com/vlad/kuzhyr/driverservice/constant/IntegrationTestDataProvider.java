package com.vlad.kuzhyr.driverservice.constant;

import com.vlad.kuzhyr.driverservice.persistence.entity.Gender;
import com.vlad.kuzhyr.driverservice.web.dto.request.CarRequest;
import com.vlad.kuzhyr.driverservice.web.dto.request.DriverRequest;
import java.util.List;

public class IntegrationTestDataProvider {

    public static final String TEST_FIRST_NAME = "John";
    public static final String TEST_LAST_NAME = "Doe";
    public static final String TEST_PHONE = "+375331928321";
    public static final String TEST_EMAIL = "example@gmail.com";

    public static final String TEST_FIRST_NAME2 = "Jane";
    public static final String TEST_LAST_NAME2 = "Smith";
    public static final String TEST_PHONE2 = "+375290928543";
    public static final String TEST_EMAIL2 = "jane.smith@example.com";

    public static final String TEST_FIRST_NAME3 = "Alice";
    public static final String TEST_LAST_NAME3 = "Johnson";
    public static final String TEST_PHONE3 = "+375332923132";
    public static final String TEST_EMAIL3 = "alice.johnson@example.com";

    public static final String TEST_FIRST_NAME4 = "Alice";
    public static final String TEST_LAST_NAME4 = "Brown";
    public static final String TEST_PHONE4 = "+375332924232";
    public static final String TEST_EMAIL4 = "alice.brown@example.com";

    public static final String TEST_FIRST_NAME5 = "Bob";
    public static final String TEST_LAST_NAME5 = "Marley";
    public static final String TEST_PHONE5 = "+375332469252";
    public static final String TEST_EMAIL5 = "bob.marley@example.com";

    public static final String TEST_FIRST_NAME6 = "Masha";
    public static final String TEST_LAST_NAME6 = "Masha";
    public static final String TEST_PHONE6 = "+375332465123";
    public static final String TEST_EMAIL6 = "masha@example.com";

    public static final String TEST_FIRST_NAME7 = "Vlad";
    public static final String TEST_LAST_NAME7 = "Inko";
    public static final String TEST_PHONE7 = "+375333452553";
    public static final String TEST_EMAIL7 = "vlad@example.com";

    public static final String TEST_COLOR = "red";
    public static final String TEST_CAR_BRAND = "Mercedes";
    public static final String TEST_CAR_NUMBER = "К092НХ07";

    public static final String TEST_COLOR2 = "blue";
    public static final String TEST_CAR_BRAND2 = "BMW";
    public static final String TEST_CAR_NUMBER2 = "А123ВС45";

    public static final String TEST_COLOR3 = "black";
    public static final String TEST_CAR_BRAND3 = "Audi";
    public static final String TEST_CAR_NUMBER3 = "Х927УК06";

    public static final String TEST_COLOR4 = "white";
    public static final String TEST_CAR_BRAND4 = "Toyota";
    public static final String TEST_CAR_NUMBER4 = "М654ОР12";

    public static final String TEST_COLOR5 = "green";
    public static final String TEST_CAR_BRAND5 = "Ford";
    public static final String TEST_CAR_NUMBER5 = "В321ТУ98";

    public static DriverRequest driverRequest() {
        return DriverRequest.builder()
            .firstName(TEST_FIRST_NAME)
            .lastName(TEST_LAST_NAME)
            .phone(TEST_PHONE)
            .email(TEST_EMAIL)
            .gender(Gender.MALE)
            .carIds(List.of())
            .build();
    }

    public static DriverRequest driverRequest2() {
        return DriverRequest.builder()
            .firstName(TEST_FIRST_NAME2)
            .lastName(TEST_LAST_NAME2)
            .phone(TEST_PHONE2)
            .email(TEST_EMAIL2)
            .gender(Gender.FEMALE)
            .carIds(List.of())
            .build();
    }

    public static DriverRequest driverRequest3() {
        return DriverRequest.builder()
            .firstName(TEST_FIRST_NAME3)
            .lastName(TEST_LAST_NAME3)
            .phone(TEST_PHONE3)
            .email(TEST_EMAIL3)
            .gender(Gender.FEMALE)
            .carIds(List.of())
            .build();
    }

    public static DriverRequest driverRequest4() {
        return DriverRequest.builder()
            .firstName(TEST_FIRST_NAME6)
            .lastName(TEST_LAST_NAME6)
            .phone(TEST_PHONE6)
            .email(TEST_EMAIL6)
            .gender(Gender.FEMALE)
            .carIds(List.of())
            .build();
    }

    public static DriverRequest driverRequest5() {
        return DriverRequest.builder()
            .firstName(TEST_FIRST_NAME7)
            .lastName(TEST_LAST_NAME7)
            .phone(TEST_PHONE7)
            .email(TEST_EMAIL7)
            .gender(Gender.FEMALE)
            .carIds(List.of())
            .build();
    }

    public static DriverRequest updatedDriverRequest() {
        return DriverRequest.builder()
            .firstName(TEST_FIRST_NAME4)
            .lastName(TEST_LAST_NAME4)
            .phone(TEST_PHONE4)
            .email(TEST_EMAIL4)
            .gender(Gender.FEMALE)
            .carIds(List.of())
            .build();
    }

    public static DriverRequest deleteDriverRequest() {
        return DriverRequest.builder()
            .firstName(TEST_FIRST_NAME5)
            .lastName(TEST_LAST_NAME5)
            .phone(TEST_PHONE5)
            .email(TEST_EMAIL5)
            .gender(Gender.MALE)
            .carIds(List.of())
            .build();
    }

    public static DriverRequest invalidDriverRequest() {
        return DriverRequest.builder()
            .firstName("Ivan")
            .lastName("Ivanov")
            .phone("123456789")
            .email("ivan@gmail.com")
            .gender(Gender.MALE)
            .carIds(List.of())
            .build();
    }

    public static CarRequest carRequest() {
        return CarRequest.builder()
            .color(TEST_COLOR)
            .carBrand(TEST_CAR_BRAND)
            .carNumber(TEST_CAR_NUMBER)
            .build();
    }

    public static CarRequest carRequest2() {
        return CarRequest.builder()
            .color(TEST_COLOR2)
            .carBrand(TEST_CAR_BRAND2)
            .carNumber(TEST_CAR_NUMBER2)
            .build();
    }

    public static CarRequest carRequest3() {
        return CarRequest.builder()
            .color(TEST_COLOR3)
            .carBrand(TEST_CAR_BRAND3)
            .carNumber(TEST_CAR_NUMBER3)
            .build();
    }

    public static CarRequest carRequest4() {
        return CarRequest.builder()
            .color(TEST_COLOR4)
            .carBrand(TEST_CAR_BRAND4)
            .carNumber(TEST_CAR_NUMBER4)
            .build();
    }

    public static CarRequest carRequest5() {
        return CarRequest.builder()
            .color(TEST_COLOR5)
            .carBrand(TEST_CAR_BRAND5)
            .carNumber(TEST_CAR_NUMBER5)
            .build();
    }

    public static CarRequest updatedCarRequest() {
        return CarRequest.builder()
            .color(TEST_COLOR2)
            .carBrand(TEST_CAR_BRAND2)
            .carNumber(TEST_CAR_NUMBER2)
            .build();
    }

    public static CarRequest deleteCarRequest() {
        return CarRequest.builder()
            .color(TEST_COLOR3)
            .carBrand(TEST_CAR_BRAND3)
            .carNumber(TEST_CAR_NUMBER3)
            .build();
    }

    public static CarRequest invalidCarRequest() {
        return CarRequest.builder()
            .color("")
            .carBrand("")
            .carNumber("")
            .build();
    }

}
