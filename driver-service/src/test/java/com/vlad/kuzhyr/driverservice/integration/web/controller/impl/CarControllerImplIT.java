package com.vlad.kuzhyr.driverservice.integration.web.controller.impl;

import com.vlad.kuzhyr.driverservice.config.TestContainerConfig;
import com.vlad.kuzhyr.driverservice.constant.ControllerRouteConstant;
import com.vlad.kuzhyr.driverservice.constant.IntegrationTestDataProvider;
import com.vlad.kuzhyr.driverservice.persistence.repository.CarRepository;
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
public class CarControllerImplIT {

    @LocalServerPort
    private int port;

    @Autowired
    private CarRepository carRepository;

    Long carId;

    @BeforeEach
    void setUp() {
        carRepository.deleteAll();
        carId = carRepository.save(IntegrationTestDataProvider.createCar()).getId();

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void testGetCarById() {
        given()
            .when()
            .get(ControllerRouteConstant.GET_CAR_BY_ID_URL.formatted(carId))
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", equalTo(carId.intValue()))
            .body("color", equalTo(IntegrationTestDataProvider.BASE_COLOR))
            .body("car_brand", equalTo(IntegrationTestDataProvider.BASE_BRAND))
            .body("car_number", equalTo(IntegrationTestDataProvider.BASE_CAR_NUMBER));
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
    void testGetAllCars() {
        CarRequest car = IntegrationTestDataProvider.carRequest();

        given().contentType(ContentType.JSON).body(car).post(ControllerRouteConstant.CREATE_CAR_URL);

        given()
            .queryParam("current_page", 0)
            .queryParam("limit", 2)
            .when()
            .get(ControllerRouteConstant.GET_ALL_CAR_URL)
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("content.size()", equalTo(2))
            .body("total_pages", greaterThanOrEqualTo(1))
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
    void testUpdateCar() {
        CarRequest updatedCarRequest = IntegrationTestDataProvider.updatedCarRequest();

        given()
            .contentType(ContentType.JSON)
            .body(updatedCarRequest)
            .when()
            .put(ControllerRouteConstant.UPDATE_CAR_URL.formatted(carId))
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", equalTo(carId.intValue()))
            .body("color", equalTo(updatedCarRequest.color()))
            .body("car_brand", equalTo(updatedCarRequest.carBrand()))
            .body("car_number", equalTo(updatedCarRequest.carNumber()));
    }

    @Test
    void testDeleteCar() {
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
