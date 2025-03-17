package com.vlad.kuzhyr.rideservice.e2e.step;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.vlad.kuzhyr.rideservice.constant.ControllerRouteConstant;
import com.vlad.kuzhyr.rideservice.constant.E2eConstant;
import com.vlad.kuzhyr.rideservice.constant.WireMockStubs;
import com.vlad.kuzhyr.rideservice.persistence.entity.Ride;
import com.vlad.kuzhyr.rideservice.persistence.entity.RideStatus;
import com.vlad.kuzhyr.rideservice.persistence.repository.RideRepository;
import com.vlad.kuzhyr.rideservice.web.dto.request.UpdateRideStatusRequest;
import com.vlad.kuzhyr.rideservice.web.dto.response.RideResponse;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import lombok.RequiredArgsConstructor;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
public class RideStep {

    @LocalServerPort
    private int port;

    private final RideRepository rideRepository;

    private Response response;
    private RequestSpecification request;
    private Long rideId;

    @Given("a configured service")
    public void configuratorServer() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        request = RestAssured.given().contentType("application/json");

        WireMock.resetAllRequests();

        WireMockStubs.mockMapboxGeocode();
        WireMockStubs.mockMapboxDistance();
        WireMockStubs.mockDriverService();
        WireMockStubs.mockPassengerService();

        rideRepository.deleteAll();

        Integer id = given()
            .contentType(ContentType.JSON)
            .body(E2eConstant.createRideRequest())
            .when()
            .post(ControllerRouteConstant.CREATE_RIDE_URL)
            .then()
            .assertThat()
            .statusCode(201)
            .extract()
            .path("id");

        rideId = Long.valueOf(id);
    }

    @Given("a ride exists")
    public void aRideExistsWithID() {
        assertTrue(rideRepository.existsById(rideId));
    }

    @Given("a ride exists with driver ID {int}")
    public void aRideExistsWithDriverID(int driverId) {
        assertTrue(rideRepository.existsRidesByDriverId((long) driverId));
    }

    @Given("a ride exists by passenger ID {int}")
    public void aRideExistsByPassengerID(int passengerId) {
        assertTrue(rideRepository.existsRidesByPassengerId((long) passengerId));
    }

    @Given("at least one ride exists")
    public void atLeastOneRideExists() {
        List<Ride> rides = rideRepository.findAll(PageRequest.of(0, 10)).getContent();
        assertFalse(rides.isEmpty());
    }

    @When("I create a ride with the following details:")
    public void iCreateARideWithTheFollowingDetails(String rideRequest) {
        response = request.body(rideRequest).post(ControllerRouteConstant.CREATE_RIDE_URL);
    }

    @When("I get the ride with ID {int}")
    public void iGetTheRideWithID(int rideId) {
        response = request.get(ControllerRouteConstant.GET_RIDE_BY_ID_URL.formatted(rideId));
    }

    @When("I update the ride with the following details:")
    public void iUpdateTheRideWithIDWithTheFollowingDetails(String updateRideRequest) {
        response = request.body(updateRideRequest).put(ControllerRouteConstant.UPDATE_RIDE_URL.formatted(rideId));
    }

    @When("I update the ride status to {string}")
    public void iUpdateTheRideStatusWithIDTo(String status) {
        UpdateRideStatusRequest updateRideStatusRequest = new UpdateRideStatusRequest(RideStatus.valueOf(status));
        response = request.body(updateRideStatusRequest)
            .patch(ControllerRouteConstant.UPDATE_RIDE_STATUS_URL.formatted(rideId));
    }

    @When("I request get all rides by driver ID {int}, current_page {int} and limit {int}")
    public void iRequestGetAllRidesByDriverID(int driverId, int currentPage, int limit) {
        response = request
            .queryParam("limit", limit)
            .queryParam("current_page", currentPage)
            .get(ControllerRouteConstant.GET_ALL_RIDES_BY_DRIVER_ID_URL.formatted(driverId));
    }

    @When("I request get all rides by passenger ID {int}, current_page {int} and limit {int}")
    public void iRequestGetAllRidesByPassengerID(int passengerId, int currentPage, int limit) {
        response = request
            .queryParam("limit",limit)
            .queryParam("current_page", currentPage)
            .get(ControllerRouteConstant.GET_ALL_RIDES_BY_PASSENGER_ID_URL.formatted(passengerId));
    }

    @When("I request get all rides, current_page {int} and limit {int}")
    public void iRequestGetAllRides(int currentPage, int limit) {
        response = request
            .queryParam("limit", limit)
            .queryParam("current_page", currentPage)
            .get(ControllerRouteConstant.GET_ALL_RIDES_URL);
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int statusCode) {
        assertEquals(statusCode, response.statusCode());
    }

    @And("the response should contain at least one ride")
    public void theResponseShouldContainAtLeastOneRide() {
        assertNotNull(response);
        response.then().body("content.size()", greaterThanOrEqualTo(1));
    }

    @And("the response should contain the ride details")
    public void theResponseShouldContainTheRideDetails() {
        assertNotNull(response);
        RideResponse rideResponse = response.as(RideResponse.class);
        assertNotNull(rideResponse.id());
        assertNotNull(rideResponse.departureAddress());
        assertNotNull(rideResponse.destinationAddress());
        assertNotNull(rideResponse.driverId());
        assertNotNull(rideResponse.passengerId());
    }

    @And("the response should contain the updated ride status")
    public void theResponseShouldContainTheUpdatedRideStatus() {
        assertNotNull(response);
        RideResponse rideResponse = response.as(RideResponse.class);
        assertNotNull(rideResponse.rideStatus());
    }

    @And("the response should contain the updated ride details")
    public void theResponseShouldContainTheUpdatedRideDetails() {
        assertNotNull(response);
        RideResponse rideResponse = response.as(RideResponse.class);
        assertNotNull(rideResponse.departureAddress());
        assertNotNull(rideResponse.destinationAddress());
    }

}

