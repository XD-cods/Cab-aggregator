package com.vlad.kuzhyr.passengerservice.e2e.steps;

import com.vlad.kuzhyr.passengerservice.constant.E2EConstant;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PassengerSteps {

    private Response response;
    private Long passengerId;
    private String requestBody;

    @Given("a passenger request body:")
    public void iHaveAPassengerRequestBody(String body) {
        requestBody = body;
    }

    @Given("a passenger by id: {int}")
    public void aPassengerById(int id) {
        passengerId = (long) id;
    }

    @When("I send a request to create a new passenger")
    public void createPassenger() {
        response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .post(E2EConstant.PASSENGERS_URL);
    }

    @When("I send a request to get the passenger by ID")
    public void getPassengerById() {
        response = RestAssured.given()
            .get(E2EConstant.PASSENGERS_URL + "/" + passengerId);
    }

    @When("I send a request to update the passenger details")
    public void updatePassenger() {
        response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .put(E2EConstant.PASSENGERS_URL + "/" + passengerId);
    }

    @When("I send a request to delete the passenger")
    public void deletePassenger() {
        response = RestAssured.given()
            .delete(E2EConstant.PASSENGERS_URL + "/" + passengerId);
    }

    @Then("I should get the passenger details in the response")
    public void checkPassengerDetails() {
        assertThat(response.statusCode(), is(HttpStatus.OK.value()));
        assertThat(response.jsonPath().getString("id"), is(passengerId.toString()));
    }

    @Then("the passenger details should be updated")
    public void checkPassengerUpdated() {
        assertThat(response.statusCode(), is(HttpStatus.OK.value()));
        assertThat(response.jsonPath().getString("first_name"), is("UpdatedFirstName"));
        assertThat(response.jsonPath().getString("last_name"), is("UpdatedLastName"));
    }

    @Then("I should get a passenger response with status {int}")
    public void checkResponseStatus(int statusCode) {
        assertThat(response.statusCode(), is(statusCode));
    }
}