package com.vlad.kuzhyr.driverservice.e2e.steps;

import com.vlad.kuzhyr.driverservice.constant.E2eConstant;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class DriverSteps {

    private Response response;
    private Long driverId;
    private String requestBody;

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
            .post(E2eConstant.DRIVERS_URL);
    }

    @When("I send a request to get the driver by ID")
    public void getDriverById() {
        response = RestAssured.given()
            .get(E2eConstant.DRIVERS_URL + "/" + driverId);
    }

    @When("I send a request to update the driver details")
    public void updateDriver() {
        response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .put(E2eConstant.DRIVERS_URL + "/" + driverId);
    }

    @When("I send a request to delete the driver")
    public void deleteDriver() {
        response = RestAssured.given()
            .delete(E2eConstant.DRIVERS_URL + "/" + driverId);
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
