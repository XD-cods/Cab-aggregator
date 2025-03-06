package com.vlad.kuzhyr.ratingservice.e2e.step;

import com.vlad.kuzhyr.ratingservice.config.E2EConstant;
import com.vlad.kuzhyr.ratingservice.persistence.entity.RatedBy;
import com.vlad.kuzhyr.ratingservice.persistence.entity.Rating;
import com.vlad.kuzhyr.ratingservice.persistence.entity.RideInfo;
import com.vlad.kuzhyr.ratingservice.persistence.repository.RatingRepository;
import com.vlad.kuzhyr.ratingservice.persistence.repository.RideInfoRepository;
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

@RequiredArgsConstructor
public class RatingStep {

    private final RideInfoRepository rideInfoRepository;
    private final RatingRepository ratingRepository;

    private Response response;
    private Long ratingId;
    private String requestBody;
    private RideInfo rideInfo;

    @Given("a rating request body:")
    public void iHaveARatingRequestBody(String body) {
        requestBody = body;
    }

    @Given("a rating by id: {int}")
    public void aRatingById(int id) {
        ratingId = (long) id;
    }

    @Given("ride info exists with ride id {long}")
    public void rideInfoExists(long rideId) {
        if (!rideInfoRepository.existsById(rideId)) {
            RideInfo rideInfo = new RideInfo();
            rideInfo.setRideId(rideId);
            rideInfo.setPassengerId(1L);
            rideInfo.setDriverId(1L);
            rideInfoRepository.save(rideInfo);
        }
        rideInfo = rideInfoRepository.findByRideId(rideId).orElseThrow();
    }

    @Given("a rating exists with id {long}")
    public void ratingExists(long rateId) {
        if (!ratingRepository.existsByRideInfo_RideIdAndRatedBy(rateId, RatedBy.PASSENGER)) {
            Rating rating = new Rating();
            rating.setId(ratingId);
            rating.setRideInfo(rideInfo);
            rating.setRating(5.0);
            rating.setRatedBy(RatedBy.PASSENGER);
            rating.setComment("Great service!");
            ratingRepository.save(rating);
        }
        ratingId = rateId;
    }

    @Given("a rating exists for passenger {long} with id {long}")
    public void ratingExistsForPassenger(long passengerId, long ratingId) {
        if(!rideInfoRepository.existsById(2L)) {
            RideInfo rideInfo = new RideInfo();
            rideInfo.setRideId(2L);
            rideInfo.setPassengerId(passengerId);
            rideInfo.setDriverId(2L);
            rideInfoRepository.save(rideInfo);
        }

        Rating rating = new Rating();
        rating.setId(ratingId);
        rating.setRideInfo(rideInfo);
        rating.setRating(5.0);
        rating.setComment("Great service!");
        rating.setRatedBy(RatedBy.DRIVER);
        ratingRepository.save(rating);
    }

    @Given("a rating exists for driver {long} with id {long} and rating {double}")
    public void ratingExistsForDriver(long driverId, long ratingId, double ratingValue) {
        if(rideInfoRepository.existsById(3L)) {
            RideInfo rideInfo = new RideInfo();
            rideInfo.setRideId(3L);
            rideInfo.setPassengerId(3L);
            rideInfo.setDriverId(driverId);
            rideInfoRepository.save(rideInfo);
        }

        Rating rating = new Rating();
        rating.setId(ratingId);
        rating.setRideInfo(rideInfo);
        rating.setRating(ratingValue);
        rating.setComment("Great service!");
        rating.setRatedBy(RatedBy.PASSENGER);
        ratingRepository.save(rating);
    }

    @When("I send a request to create a new rating")
    public void createRating() {
        response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .post(E2EConstant.RATINGS_URL);
    }

    @When("I send a request to get the rating by ID")
    public void getRatingById() {
        if (ratingId == null) {
            ratingId = 0L;
        }

        response = RestAssured.given()
            .get(E2EConstant.RATINGS_URL + "/" + ratingId);
    }

    @When("I send a request to update the rating")
    public void updateRating() {
        response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .put(E2EConstant.RATINGS_URL + "/" + ratingId);
    }

    @When("I send a request to get the average rating by passenger ID: {int}")
    public void getAverageRatingByPassengerId(int passengerId) {
        response = RestAssured.given()
            .get(E2EConstant.RATINGS_URL + "/passenger/" + (long) passengerId);
    }

    @When("I send a request to get the average rating by driver ID: {int}")
    public void getAverageRatingByDriverId(int driverId) {
        response = RestAssured.given()
            .get(E2EConstant.RATINGS_URL + "/driver/" + (long) driverId);
    }

    @When("I send a request to get all ratings with page {int} and limit {int}")
    public void getAllRatings(int page, int limit) {
        response = RestAssured.given()
            .queryParam("current_page", page)
            .queryParam("limit", limit)
            .get(E2EConstant.RATINGS_URL);
    }

    @Then("I should get the rating details in the response")
    public void checkRatingDetails() {
        assertThat(response.statusCode(), is(HttpStatus.OK.value()));
        assertThat(response.jsonPath().getString("id"), is(ratingId.toString()));
    }

    @Then("I should get the average rating in the response")
    public void checkAverageRating() {
        assertThat(response.statusCode(), is(HttpStatus.OK.value()));
    }

    @Then("I should get a rating response with status {int}")
    public void checkResponseStatus(int statusCode) {
        assertThat(response.statusCode(), is(statusCode));
    }
}
