package com.vlad.kuzhyr.passengerservice.integration.web.controller.impl;

import com.vlad.kuzhyr.passengerservice.config.TestContainerConfig;
import com.vlad.kuzhyr.passengerservice.constant.ControllerRouteConstant;
import com.vlad.kuzhyr.passengerservice.constant.IntegrationTestDataProvider;
import com.vlad.kuzhyr.passengerservice.persistence.repository.PassengerRepository;
import com.vlad.kuzhyr.passengerservice.web.dto.request.PassengerRequest;
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
public class PassengerControllerImplIT {

    @LocalServerPort
    private int port;

    @Autowired
    private PassengerRepository passengerRepository;

    Long passengerId;

    @BeforeEach
    void setUp() {
        passengerRepository.deleteAll();
        passengerId = passengerRepository.save(IntegrationTestDataProvider.createPassenger()).getId();

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void testGetPassengerById() {
        given()
            .when()
            .get(ControllerRouteConstant.GET_PASSENGER_BY_ID_URL.formatted(passengerId))
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", equalTo(passengerId.intValue()))
            .body("first_name", equalTo(IntegrationTestDataProvider.BASE_FIRSTNAME))
            .body("last_name", equalTo(IntegrationTestDataProvider.BASE_LASTNAME))
            .body("phone", equalTo(IntegrationTestDataProvider.BASE_PHONE))
            .body("email", equalTo(IntegrationTestDataProvider.BASE_EMAIL));
    }

    @Test
    void testGetPassengerById_shouldReturnNotFound() {
        given()
            .when()
            .get(ControllerRouteConstant.GET_PASSENGER_BY_ID_URL.formatted(0))
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void testGetAllPassengers() {
        PassengerRequest passenger = IntegrationTestDataProvider.passengerRequest();

        given().contentType(ContentType.JSON).body(passenger).post(ControllerRouteConstant.CREATE_PASSENGER_URL);

        given()
            .queryParam("current_page", 0)
            .queryParam("limit", 2)
            .when()
            .get(ControllerRouteConstant.GET_PASSENGERS_URL)
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("content.size()", equalTo(2))
            .body("total_pages", greaterThanOrEqualTo(1))
            .body("total_elements", greaterThanOrEqualTo(2));
    }

    @Test
    void testGetAllPassengers_whenLimitNegative_shouldReturnBadRequest() {
        given()
            .queryParam("current_page", 0)
            .queryParam("limit", -1)
            .when()
            .get(ControllerRouteConstant.GET_PASSENGERS_URL)
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void testCreatePassenger() {
        PassengerRequest passengerRequest = IntegrationTestDataProvider.passengerRequest();

        given()
            .contentType(ContentType.JSON)
            .body(passengerRequest)
            .when()
            .post(ControllerRouteConstant.CREATE_PASSENGER_URL)
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .body("id", notNullValue())
            .body("first_name", equalTo(passengerRequest.firstName()))
            .body("last_name", equalTo(passengerRequest.lastName()))
            .body("phone", equalTo(passengerRequest.phone()))
            .body("email", equalTo(passengerRequest.email()));
    }

    @Test
    void testCreatePassenger_whenPassengerRequestNotValid_shouldThrowException() {
        PassengerRequest passengerRequest = IntegrationTestDataProvider.invalidPassengerRequest();

        given()
            .contentType(ContentType.JSON)
            .body(passengerRequest)
            .when()
            .post(ControllerRouteConstant.CREATE_PASSENGER_URL)
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void testUpdatePassenger() {
        PassengerRequest updatedPassengerRequest = IntegrationTestDataProvider.updatedPassengerRequest();

        given()
            .contentType(ContentType.JSON)
            .body(updatedPassengerRequest)
            .when()
            .put(ControllerRouteConstant.UPDATE_PASSENGER_URL.formatted(passengerId))
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", equalTo(passengerId.intValue()))
            .body("first_name", equalTo(updatedPassengerRequest.firstName()))
            .body("last_name", equalTo(updatedPassengerRequest.lastName()))
            .body("phone", equalTo(updatedPassengerRequest.phone()))
            .body("email", equalTo(updatedPassengerRequest.email()));
    }

    @Test
    void testDeletePassenger() {
        given()
            .when()
            .delete(ControllerRouteConstant.DELETE_PASSENGER_URL.formatted(passengerId))
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body(equalTo("true"));

        given()
            .when()
            .get(ControllerRouteConstant.GET_PASSENGER_BY_ID_URL.formatted(passengerId))
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}
