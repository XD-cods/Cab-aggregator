package com.vlad.kuzhyr.rideservice.integration.web.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.vlad.kuzhyr.rideservice.config.TestContainerConfig;
import com.vlad.kuzhyr.rideservice.constant.ControllerRouteConstant;
import com.vlad.kuzhyr.rideservice.constant.IntegrationTestDataProvider;
import com.vlad.kuzhyr.rideservice.persistence.entity.Address;
import com.vlad.kuzhyr.rideservice.persistence.entity.RideStatus;
import com.vlad.kuzhyr.rideservice.persistence.repository.AddressRepository;
import com.vlad.kuzhyr.rideservice.persistence.repository.RideRepository;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
@ImportTestcontainers(TestContainerConfig.class)
@EmbeddedKafka
public class RideControllerImplIT {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Long rideId;

    @BeforeEach
    public void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        mockDriverService();
        mockPassengerService();

        rideRepository.deleteAll();
        addressRepository.deleteAll();

        addressRepository.save(
            new Address(1L, IntegrationTestDataProvider.BASE_DEPARTURE_ADDRESS, 40.7128,
                -74.0060));
        addressRepository.save(
            new Address(2L, IntegrationTestDataProvider.BASE_DESTINATION_ADDRESS, 34.0522,
                -118.2437));

        rideId = given()
            .contentType(ContentType.JSON)
            .body(IntegrationTestDataProvider.createRideRequest())
            .when()
            .post(ControllerRouteConstant.CREATE_RIDE_URL)
            .then()
            .extract()
            .path("id");
    }

    private void mockDriverService() {
        String driverResponse = """
            {
                "id": 1,
                "first_name": "John",
                "last_name": "Doe",
                "email": "john.doe@example.com",
                "gender": "MALE",
                "phone": "+375335184521",
                "car_ids": [1, 2],
                "is_enabled": true,
                "is_busy": false
            }
            """;
        WireMock.stubFor(WireMock.get("/api/v1/drivers/1")
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(driverResponse)));
    }

    private void mockPassengerService() {
        String passengerResponse = """
            {
                "id": 1,
                "first_name": "Jane",
                "last_name": "Doe",
                "email": "jane.doe@example.com",
                "phone": "+375295162318",
                "is_enabled": true,
                "is_busy": false
            }
            """;
        WireMock.stubFor(WireMock.get("/api/v1/passengers/1")
            .willReturn(WireMock.aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(passengerResponse)));
    }

    @Test
    public void testCreateRide() {
        addressRepository.save(
            new Address(1L, IntegrationTestDataProvider.BASE_DEPARTURE_ADDRESS2, 42.7128,
                -73.0060));
        addressRepository.save(
            new Address(2L, IntegrationTestDataProvider.BASE_DESTINATION_ADDRESS2, 36.0522,
                -128.2437));

        given()
            .contentType(ContentType.JSON)
            .body(IntegrationTestDataProvider.createRideRequest2())
            .when()
            .post(ControllerRouteConstant.CREATE_RIDE_URL)
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("driver_id", equalTo(IntegrationTestDataProvider.BASE_DRIVER_ID.intValue()))
            .body("passenger_id", equalTo(IntegrationTestDataProvider.BASE_PASSENGER_ID.intValue()));
    }

    @Test
    public void testUpdateRide() {
        given()
            .contentType(ContentType.JSON)
            .body(IntegrationTestDataProvider.createUpdateRideRequest())
            .when()
            .put(String.format(ControllerRouteConstant.UPDATE_RIDE_URL, rideId))
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("departureAddress.addressName", equalTo("улица Воровского, 20, Брест, Беларусь"))
            .body("destinationAddress.addressName", equalTo("проспект Машерова, 67, Брест, Беларусь"));
    }

    @Test
    public void testUpdateRideStatus() {
        given()
            .contentType(ContentType.JSON)
            .body(IntegrationTestDataProvider.createUpdateRideStatusRequest())
            .when()
            .patch(String.format(ControllerRouteConstant.UPDATE_RIDE_STATUS_URL, rideId))
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("ride_status", equalTo(RideStatus.WAITING_FOR_DRIVER.toString()));
    }

    @Test
    public void testGetRideById() {
        given()
            .contentType(ContentType.JSON)
            .when()
            .get(String.format(ControllerRouteConstant.GET_RIDE_BY_ID_URL, rideId))
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("id", equalTo(rideId.intValue()));
    }

    @Test
    public void testGetAllRidesByDriverId() {
        given()
            .contentType(ContentType.JSON)
            .when()
            .get(String.format(ControllerRouteConstant.GET_ALL_RIDES_BY_DRIVER_ID_URL,
                IntegrationTestDataProvider.BASE_DRIVER_ID) + "?current_page=0&limit=10")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("content.size()", equalTo(1));
    }

    @Test
    public void testGetAllRidesByPassengerId() {
        given()
            .contentType(ContentType.JSON)
            .when()
            .get(String.format(ControllerRouteConstant.GET_ALL_RIDES_BY_PASSENGER_ID_URL,
                IntegrationTestDataProvider.BASE_PASSENGER_ID) + "?current_page=0&limit=10")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("content.size()", equalTo(1));
    }

    @Test
    public void testGetAllRides() {
        given()
            .contentType(ContentType.JSON)
            .when()
            .get(ControllerRouteConstant.GET_ALL_RIDES_URL + "?current_page=0&limit=10")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("content.size()", equalTo(1));
    }
}