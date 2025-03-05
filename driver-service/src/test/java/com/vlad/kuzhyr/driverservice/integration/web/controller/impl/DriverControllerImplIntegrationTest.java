package com.vlad.kuzhyr.driverservice.integration.web.controller.impl;

import com.vlad.kuzhyr.driverservice.constant.ControllerRouteConstant;
import com.vlad.kuzhyr.driverservice.constant.IntegrationTestDataProvider;
import com.vlad.kuzhyr.driverservice.web.dto.request.DriverRequest;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
public class DriverControllerImplIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine")
        .withUsername("postgres")
        .withPassword("1111");

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        System.setProperty("DB_PORT", String.valueOf(postgres.getFirstMappedPort()));
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
    void testGetAllDrivers() {
        DriverRequest driver1 = IntegrationTestDataProvider.driverRequest4();
        DriverRequest driver2 = IntegrationTestDataProvider.driverRequest5();

        given().contentType(ContentType.JSON).body(driver1).post(ControllerRouteConstant.CREATE_DRIVER_URL);
        given().contentType(ContentType.JSON).body(driver2).post(ControllerRouteConstant.CREATE_DRIVER_URL);

        given()
            .queryParam("current_page", 0)
            .queryParam("limit", 1)
            .when()
            .get(ControllerRouteConstant.GET_ALL_DRIVERS_URL)
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("content.size()", equalTo(1))
            .body("total_pages", greaterThanOrEqualTo(2))
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
    void testGetDriverById() {
        DriverRequest driverRequest = IntegrationTestDataProvider.driverRequest2();

        Integer driverId = given()
            .contentType(ContentType.JSON)
            .body(driverRequest)
            .when()
            .post(ControllerRouteConstant.CREATE_DRIVER_URL)
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .extract()
            .path("id");

        given()
            .when()
            .get(ControllerRouteConstant.GET_DRIVER_BY_ID_URL.formatted(driverId))
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", equalTo(driverId))
            .body("first_name", equalTo(driverRequest.firstName()))
            .body("last_name", equalTo(driverRequest.lastName()))
            .body("email", equalTo(driverRequest.email()))
            .body("phone", equalTo(driverRequest.phone()));
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
    void testUpdateDriver() {
        DriverRequest driverRequest = IntegrationTestDataProvider.driverRequest3();

        Integer driverId = given()
            .contentType(ContentType.JSON)
            .body(driverRequest)
            .when()
            .post(ControllerRouteConstant.CREATE_DRIVER_URL)
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .extract()
            .path("id");

        DriverRequest updatedDriverRequest = IntegrationTestDataProvider.updatedDriverRequest();

        given()
            .contentType(ContentType.JSON)
            .body(updatedDriverRequest)
            .when()
            .put(ControllerRouteConstant.UPDATE_DRIVER_URL.formatted(driverId))
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", equalTo(driverId))
            .body("first_name", equalTo(updatedDriverRequest.firstName()))
            .body("last_name", equalTo(updatedDriverRequest.lastName()))
            .body("email", equalTo(updatedDriverRequest.email()))
            .body("phone", equalTo(updatedDriverRequest.phone()));
    }

    @Test
    void testDeleteDriver() {
        DriverRequest driverRequest = IntegrationTestDataProvider.deleteDriverRequest();

        Integer driverId = given()
            .contentType(ContentType.JSON)
            .body(driverRequest)
            .when()
            .post(ControllerRouteConstant.CREATE_DRIVER_URL)
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .extract()
            .path("id");

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
