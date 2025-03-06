package com.vlad.kuzhyr.driverservice.integration.web.controller.impl;

import com.vlad.kuzhyr.driverservice.config.TestContainerConfig;
import com.vlad.kuzhyr.driverservice.constant.ControllerRouteConstant;
import com.vlad.kuzhyr.driverservice.constant.IntegrationTestDataProvider;
import com.vlad.kuzhyr.driverservice.persistence.repository.CarRepository;
import com.vlad.kuzhyr.driverservice.persistence.repository.DriverRepository;
import com.vlad.kuzhyr.driverservice.web.dto.request.DriverRequest;
import com.vlad.kuzhyr.driverservice.web.dto.request.DriverUpdateCarsRequest;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import java.util.List;
import org.apache.http.HttpStatus;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka
@ActiveProfiles("test")
@ImportTestcontainers(TestContainerConfig.class)
public class DriverControllerImplIT {

    @LocalServerPort
    private int port;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private CarRepository carRepository;

    Long driverId;

    @BeforeEach
    void setUp() {
        driverRepository.deleteAll();
        carRepository.deleteAll();
        driverId = driverRepository.save(IntegrationTestDataProvider.createDriver()).getId();

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void testGetDriverById() {
        given()
            .when()
            .get(ControllerRouteConstant.GET_DRIVER_BY_ID_URL.formatted(driverId))
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", equalTo(driverId.intValue()))
            .body("first_name", equalTo(IntegrationTestDataProvider.BASE_FIRSTNAME))
            .body("last_name", equalTo(IntegrationTestDataProvider.BASE_LASTNAME))
            .body("email", equalTo(IntegrationTestDataProvider.BASE_EMAIL))
            .body("phone", equalTo(IntegrationTestDataProvider.BASE_PHONE));
    }

    @Test
    void testGetDriverById_shouldReturnNotFound() {
        given()
            .when()
            .get(ControllerRouteConstant.GET_DRIVER_BY_ID_URL.formatted(0))
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void testGetAllDrivers() {
        DriverRequest driver = IntegrationTestDataProvider.driverRequest();

        given().contentType(ContentType.JSON).body(driver).post(ControllerRouteConstant.CREATE_DRIVER_URL);

        given()
            .queryParam("current_page", 0)
            .queryParam("limit", 2)
            .when()
            .get(ControllerRouteConstant.GET_ALL_DRIVERS_URL)
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("content.size()", equalTo(2))
            .body("total_pages", greaterThanOrEqualTo(1))
            .body("total_elements", greaterThanOrEqualTo(2));
    }

    @Test
    void testGetAllDrivers_whenLimitNegative_shouldReturnBadRequest() {
        given()
            .queryParam("current_page", 0)
            .queryParam("limit", -1)
            .when()
            .get(ControllerRouteConstant.GET_ALL_DRIVERS_URL)
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void testCreateDriver() {
        DriverRequest driverRequest = IntegrationTestDataProvider.driverRequest();

        given()
            .contentType(ContentType.JSON)
            .body(driverRequest)
            .when()
            .post(ControllerRouteConstant.CREATE_DRIVER_URL)
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .body("id", notNullValue())
            .body("first_name", equalTo(driverRequest.firstName()))
            .body("last_name", equalTo(driverRequest.lastName()))
            .body("email", equalTo(driverRequest.email()))
            .body("phone", equalTo(driverRequest.phone()));
    }

    @Test
    void testCreateDriver_whenDriverRequestNotValid_shouldThrowException() {
        DriverRequest driverRequest = IntegrationTestDataProvider.invalidDriverRequest();

        given()
            .contentType(ContentType.JSON)
            .body(driverRequest)
            .when()
            .post(ControllerRouteConstant.CREATE_DRIVER_URL)
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void testUpdateDriver() {
        DriverRequest updatedDriverRequest = IntegrationTestDataProvider.updatedDriverRequest();

        given()
            .contentType(ContentType.JSON)
            .body(updatedDriverRequest)
            .when()
            .put(ControllerRouteConstant.UPDATE_DRIVER_URL.formatted(driverId))
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", equalTo(driverId.intValue()))
            .body("first_name", equalTo(updatedDriverRequest.firstName()))
            .body("last_name", equalTo(updatedDriverRequest.lastName()))
            .body("email", equalTo(updatedDriverRequest.email()))
            .body("phone", equalTo(updatedDriverRequest.phone()));
    }

    @Test
    void testUpdateDriverCarsById() {
        Integer carId = given()
            .contentType(ContentType.JSON)
            .body(IntegrationTestDataProvider.carRequest())
            .when()
            .post(ControllerRouteConstant.CREATE_CAR_URL)
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .extract()
            .path("id");

        DriverUpdateCarsRequest updateCarsRequest = new DriverUpdateCarsRequest(List.of(Long.valueOf(carId)));

        given()
            .contentType(ContentType.JSON)
            .body(updateCarsRequest)
            .when()
            .patch(ControllerRouteConstant.UPDATE_DRIVER_CARS_URL.formatted(driverId))
            .then()
            .statusCode(HttpStatus.SC_OK);
    }

    @Test
    void testDeleteDriver() {
        given()
            .when()
            .delete(ControllerRouteConstant.DELETE_DRIVER_URL.formatted(driverId))
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body(equalTo("true"));

        given()
            .when()
            .get(ControllerRouteConstant.GET_DRIVER_BY_ID_URL.formatted(driverId))
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }

}
