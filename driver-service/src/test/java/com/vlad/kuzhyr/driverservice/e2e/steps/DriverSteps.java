package com.vlad.kuzhyr.driverservice.e2e.steps;

import com.vlad.kuzhyr.driverservice.constant.ControllerRouteConstant;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class DriverSteps {

    @LocalServerPort
    private int port;

    private Response response;
    private Long driverId;
    private String requestBody;

    @Given("a configured driver service")
    public void aConfiguredService() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Given("a driver request body:")
    public void iHaveADriverRequestBody(String body) {
        requestBody = body;
    }

    @Given("a driver by id: {int}")
    public void aDriverById(int id) {
        driverId = (long) id;
    }

    @When("I send a request to create a new driver")
    public void createDriver() {
        response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .post(ControllerRouteConstant.CREATE_DRIVER_URL);
    }

    @When("I send a request to get the driver by ID")
    public void getDriverById() {
        response = RestAssured.given()
            .get(ControllerRouteConstant.GET_DRIVER_BY_ID_URL.formatted(driverId));
    }

    @When("I send a request to update the driver details")
    public void updateDriver() {
        response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .put(ControllerRouteConstant.UPDATE_DRIVER_URL.formatted(driverId));
    }

    @When("I send a request to delete the driver")
    public void deleteDriver() {
        response = RestAssured.given()
            .delete(ControllerRouteConstant.DELETE_DRIVER_URL.formatted(driverId));
    }

    @Then("I should get the driver details in the response")
    public void checkDriverDetails() {
        assertThat(response.statusCode(), is(HttpStatus.OK.value()));
        assertThat(response.jsonPath().getString("id"), is(driverId.toString()));
    }

    @Then("the driver details should be updated")
    public void checkDriverUpdated() {
        assertThat(response.statusCode(), is(HttpStatus.OK.value()));
        assertThat(response.jsonPath().getString("first_name"), is("Updated first name"));
        assertThat(response.jsonPath().getString("last_name"), is("Updated last name"));
    }

    @Then("I should get a driver response with status {int}")
    public void checkResponseStatus(int statusCode) {
        assertThat(response.statusCode(), is(statusCode));
    }

}
