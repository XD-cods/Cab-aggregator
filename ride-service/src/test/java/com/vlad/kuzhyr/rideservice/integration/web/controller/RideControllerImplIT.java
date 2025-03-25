package com.vlad.kuzhyr.rideservice.integration.web.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.vlad.kuzhyr.rideservice.config.TestContainerConfig;
import com.vlad.kuzhyr.rideservice.constant.ControllerRouteConstant;
import com.vlad.kuzhyr.rideservice.constant.IntegrationTestDataProvider;
import com.vlad.kuzhyr.rideservice.constant.WireMockStubs;
import com.vlad.kuzhyr.rideservice.persistence.entity.RideStatus;
import com.vlad.kuzhyr.rideservice.persistence.repository.AddressRepository;
import com.vlad.kuzhyr.rideservice.persistence.repository.RideRepository;
import com.vlad.kuzhyr.rideservice.utility.client.DriverFeignClient;
import com.vlad.kuzhyr.rideservice.utility.client.MapboxClient;
import com.vlad.kuzhyr.rideservice.utility.client.PassengerFeignClient;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ImportTestcontainers(TestContainerConfig.class)
@EmbeddedKafka
@AutoConfigureWireMock(port = 0)
@ActiveProfiles("test")
public class RideControllerImplIT {

    @LocalServerPort
    private int port;

    @Autowired
    private MapboxClient mapboxClient;

    @Autowired
    private DriverFeignClient driverFeignClient;

    @Autowired
    private PassengerFeignClient passengerFeignClient;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Long rideId;
    private RequestSpecification request;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        request = RestAssured.given()
            .contentType(ContentType.JSON);

        WireMock.reset();

        WireMockStubs.mockDriverService();
        WireMockStubs.mockPassengerService();
        WireMockStubs.mockMapboxDistance();
        WireMockStubs.mockMapboxGeocode();

        rideRepository.deleteAll();

        Integer id = given()
            .contentType(ContentType.JSON)
            .body(IntegrationTestDataProvider.createRideRequest())
            .when()
            .post(ControllerRouteConstant.CREATE_RIDE_URL)
            .then()
            .assertThat()
            .statusCode(201)
            .extract()
            .path("id");

        rideId = Long.valueOf(id);
    }

    @Test
    public void testCreateRide() {
        request
            .body(IntegrationTestDataProvider.createRideRequest2())
            .when()
            .post(ControllerRouteConstant.CREATE_RIDE_URL)
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("driver_id", equalTo(IntegrationTestDataProvider.BASE_DRIVER_ID.intValue()))
            .body("passenger_id", equalTo(IntegrationTestDataProvider.BASE_PASSENGER_ID.intValue()));
    }

    @Test
    public void testCreateRide_shouldBadRequest() {
        request
            .body(IntegrationTestDataProvider.createInvalidRideRequest())
            .when()
            .post(ControllerRouteConstant.CREATE_RIDE_URL)
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testGetRideById() {
        request
            .when()
            .get(ControllerRouteConstant.GET_RIDE_BY_ID_URL.formatted(rideId))
            .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void testGetRideById_shouldNotFound() {
        request
            .when()
            .get(ControllerRouteConstant.GET_RIDE_BY_ID_URL.formatted(0L))
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void testUpdateRide() {
        request
            .body(IntegrationTestDataProvider.createUpdateRideRequest())
            .when()
            .put(ControllerRouteConstant.UPDATE_RIDE_URL.formatted(rideId))
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("departure_address", equalTo("улица Воровского, 20, Брест, Беларусь"))
            .body("destination_address", equalTo("проспект Машерова, 67, Брест, Беларусь"));
    }

    @Test
    public void testUpdateRideStatus() {
        request
            .body(IntegrationTestDataProvider.createUpdateRideStatusRequest())
            .when()
            .patch(ControllerRouteConstant.UPDATE_RIDE_STATUS_URL.formatted(rideId))
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("ride_status", equalTo(RideStatus.WAITING_FOR_DRIVER.toString()));
    }

    @Test
    public void testUpdateRideStatus_InvalidTransition() {
        request
            .body(IntegrationTestDataProvider.createInvalidUpdateRideStatusRequest())
            .when()
            .patch(ControllerRouteConstant.UPDATE_RIDE_STATUS_URL.formatted(rideId))
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testGetAllRidesByDriverId() {
        request
            .queryParam("current_page", 0)
            .queryParam("limit", 1)
            .when()
            .get(ControllerRouteConstant.GET_ALL_RIDES_BY_DRIVER_ID_URL.formatted(
                IntegrationTestDataProvider.BASE_DRIVER_ID))
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("content.size()", equalTo(1));
    }

    @Test
    public void testGetAllRidesByPassengerId() {
        request
            .queryParam("current_page", 0)
            .queryParam("limit", 1)
            .when()
            .get(ControllerRouteConstant.GET_ALL_RIDES_BY_PASSENGER_ID_URL.formatted(
                IntegrationTestDataProvider.BASE_PASSENGER_ID))
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("content.size()", equalTo(1));
    }

    @Test
    public void testGetAllRides() {
        request
            .queryParam("current_page", 0)
            .queryParam("limit", 1)
            .when()
            .get(ControllerRouteConstant.GET_ALL_RIDES_URL)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("content.size()", equalTo(1));
    }

}