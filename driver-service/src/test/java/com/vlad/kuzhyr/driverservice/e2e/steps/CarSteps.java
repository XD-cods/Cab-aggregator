package com.vlad.kuzhyr.driverservice.e2e.steps;

import com.vlad.kuzhyr.driverservice.constant.E2eConstant;
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
public class CarSteps {

    private Response response;
    private Long carId;
    private String requestBody;

    @Given("a car request body:")
    public void iHaveACarRequestBody(String body) {
        requestBody = body;
    }

    @Given("a car by id: {int}")
    public void aCarById(int id) {
        carId = (long) id;
    }

    @When("I send a request to create a new car")
    public void createCar() {
        response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .post(E2eConstant.CARS_URL);
    }

    @When("I send a request to get the car by ID")
    public void getCarById() {
        response = RestAssured.given()
            .get(E2eConstant.CARS_URL + "/" + carId);
    }

    @When("I send a request to update the car details")
    public void updateCar() {
        response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .put(E2eConstant.CARS_URL + "/" + carId);
    }

    @When("I send a request to delete the car")
    public void deleteCar() {
        response = RestAssured.given()
            .delete(E2eConstant.CARS_URL + "/" + carId);
    }

    @Then("I should get the car details in the response")
    public void checkCarDetails() {
        assertThat(response.statusCode(), is(HttpStatus.OK.value()));
        assertThat(response.jsonPath().getString("id"), is(carId.toString()));
    }

    @Then("the car details should be updated")
    public void checkCarUpdated() {
        assertThat(response.statusCode(), is(HttpStatus.OK.value()));
        assertThat(response.jsonPath().getString("color"), is("Red"));
        assertThat(response.jsonPath().getString("car_brand"), is("Tesla"));
    }

    @Then("I should get a car response with status {int}")
    public void checkResponseStatus(int statusCode) {
        assertThat(response.statusCode(), is(statusCode));
    }
}