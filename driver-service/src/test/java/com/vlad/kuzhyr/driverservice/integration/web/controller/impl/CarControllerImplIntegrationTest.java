package com.vlad.kuzhyr.driverservice.integration.web.controller.impl;

import com.vlad.kuzhyr.driverservice.constant.ControllerRouteConstant;
import com.vlad.kuzhyr.driverservice.constant.IntegrationTestDataProvider;
import com.vlad.kuzhyr.driverservice.web.dto.request.CarRequest;
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
public class CarControllerImplIntegrationTest {

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
    void testCreateCar() {
        CarRequest carRequest = IntegrationTestDataProvider.carRequest();

        given()
            .contentType(ContentType.JSON)
            .body(carRequest)
            .when()
            .post(ControllerRouteConstant.CREATE_CAR_URL)
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .body("id", notNullValue())
            .body("color", equalTo(carRequest.color()))
            .body("car_brand", equalTo(carRequest.carBrand()))
            .body("car_number", equalTo(carRequest.carNumber()));
    }

    @Test
    void testGetAllCars() {
        CarRequest car1 = IntegrationTestDataProvider.carRequest2();
        CarRequest car2 = IntegrationTestDataProvider.carRequest3();

        given().contentType(ContentType.JSON).body(car1).post(ControllerRouteConstant.CREATE_CAR_URL);
        given().contentType(ContentType.JSON).body(car2).post(ControllerRouteConstant.CREATE_CAR_URL);

        given()
            .queryParam("current_page", 0)
            .queryParam("limit", 1)
            .when()
            .get(ControllerRouteConstant.GET_ALL_CAR_URL)
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("content.size()", equalTo(1))
            .body("total_pages", greaterThanOrEqualTo(2))
            .body("total_elements", greaterThanOrEqualTo(2));
    }

    @Test
    void testGetAllCars_whenLimitNegative_shouldReturnBadRequest() {
        given()
            .queryParam("current_page", 0)
            .queryParam("limit", -1)
            .when()
            .get(ControllerRouteConstant.GET_ALL_CAR_URL)
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void testCreateCar_whenCarRequestNotValid_shouldThrowException() {
        CarRequest carRequest = IntegrationTestDataProvider.invalidCarRequest();

        given()
            .contentType(ContentType.JSON)
            .body(carRequest)
            .when()
            .post(ControllerRouteConstant.CREATE_CAR_URL)
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void testGetCarById() {
        CarRequest carRequest = IntegrationTestDataProvider.carRequest4();

        Integer carId = given()
            .contentType(ContentType.JSON)
            .body(carRequest)
            .when()
            .post(ControllerRouteConstant.CREATE_CAR_URL)
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .extract()
            .path("id");

        given()
            .when()
            .get(ControllerRouteConstant.GET_CAR_BY_ID_URL.formatted(carId))
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", equalTo(carId))
            .body("color", equalTo(carRequest.color()))
            .body("car_brand", equalTo(carRequest.carBrand()))
            .body("car_number", equalTo(carRequest.carNumber()));
    }

    @Test
    void testGetCarById_shouldReturnNotFound() {
        given()
            .when()
            .get(ControllerRouteConstant.GET_CAR_BY_ID_URL.formatted(0))
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void testUpdateCar() {
        CarRequest carRequest = IntegrationTestDataProvider.carRequest5();

        Integer carId = given()
            .contentType(ContentType.JSON)
            .body(carRequest)
            .when()
            .post(ControllerRouteConstant.CREATE_CAR_URL)
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .extract()
            .path("id");

        CarRequest updatedCarRequest = IntegrationTestDataProvider.updatedCarRequest();

        given()
            .contentType(ContentType.JSON)
            .body(updatedCarRequest)
            .when()
            .put(ControllerRouteConstant.UPDATE_CAR_URL.formatted(carId))
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", equalTo(carId))
            .body("color", equalTo(updatedCarRequest.color()))
            .body("car_brand", equalTo(updatedCarRequest.carBrand()))
            .body("car_number", equalTo(updatedCarRequest.carNumber()));
    }

    @Test
    void testDeleteCar() {
        CarRequest carRequest = IntegrationTestDataProvider.deleteCarRequest();

        Integer carId = given()
            .contentType(ContentType.JSON)
            .body(carRequest)
            .when()
            .post(ControllerRouteConstant.CREATE_CAR_URL)
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .extract()
            .path("id");

        given()
            .when()
            .delete(ControllerRouteConstant.DELETE_CAR_URL.formatted(carId))
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body(equalTo("true"));

        given()
            .when()
            .get(ControllerRouteConstant.GET_CAR_BY_ID_URL.formatted(carId))
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }

}
