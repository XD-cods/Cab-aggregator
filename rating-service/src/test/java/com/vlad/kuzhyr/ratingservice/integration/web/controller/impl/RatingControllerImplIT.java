package com.vlad.kuzhyr.ratingservice.integration.web.controller.impl;

import com.vlad.kuzhyr.ratingservice.config.TestContainerConfig;
import com.vlad.kuzhyr.ratingservice.constant.ControllerRouteConstant;
import com.vlad.kuzhyr.ratingservice.constant.IntegrationTestDataProvider;
import com.vlad.kuzhyr.ratingservice.persistence.entity.Rating;
import com.vlad.kuzhyr.ratingservice.persistence.entity.RideInfo;
import com.vlad.kuzhyr.ratingservice.persistence.repository.RatingRepository;
import com.vlad.kuzhyr.ratingservice.persistence.repository.RideInfoRepository;
import com.vlad.kuzhyr.ratingservice.web.dto.request.CreateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.dto.request.UpdateRatingRequest;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
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
public class RatingControllerImplIT {

    @LocalServerPort
    private int port;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RideInfoRepository rideInfoRepository;

    private Long ratingId;
    private Long rideInfoId;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        ratingRepository.deleteAll();
        rideInfoRepository.deleteAll();

        RideInfo rideInfo = rideInfoRepository.save(IntegrationTestDataProvider.createRideInfo());
        rideInfoId = rideInfo.getRideInfoId();

        Rating rating = ratingRepository.save(IntegrationTestDataProvider.createRating(rideInfo));
        ratingId = rating.getId();

    }

    @Test
    void testGetRatingById() {
        given()
            .when()
            .get(ControllerRouteConstant.GET_RATING_BY_ID_URL.formatted(ratingId))
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", equalTo(ratingId.intValue()))
            .body("rating", equalTo(IntegrationTestDataProvider.BASE_RATING.floatValue()))
            .body("comment", equalTo(IntegrationTestDataProvider.BASE_COMMENT))
            .body("rated_by", equalTo(IntegrationTestDataProvider.BASE_RATED_BY.toString()));
    }

    @Test
    void testGetRatingById_shouldReturnNotFound() {
        given()
            .when()
            .get(ControllerRouteConstant.GET_RATING_BY_ID_URL.formatted(0))
            .then()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void testGetAllRatings() {
        given()
            .queryParam("current_page", 0)
            .queryParam("limit", 2)
            .when()
            .get(ControllerRouteConstant.GET_RATINGS_URL)
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("content.size()", equalTo(1))
            .body("total_pages", greaterThanOrEqualTo(1))
            .body("total_elements", greaterThanOrEqualTo(1));
    }

    @Test
    void testGetAllRatings_whenLimitNegative_shouldReturnBadRequest() {
        given()
            .queryParam("current_page", 0)
            .queryParam("limit", -1)
            .when()
            .get(ControllerRouteConstant.GET_RATINGS_URL)
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void testCreateRating_whenRatingRequestNotValid_shouldThrowException() {
        CreateRatingRequest invalidCreateRatingRequest = IntegrationTestDataProvider.invalidCreateRatingRequest();

        given()
            .contentType(ContentType.JSON)
            .body(invalidCreateRatingRequest)
            .when()
            .post(ControllerRouteConstant.CREATE_RATING_URL)
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    void testUpdateRating() {
        UpdateRatingRequest updateRatingRequest = IntegrationTestDataProvider.updateRatingRequest();

        given()
            .contentType(ContentType.JSON)
            .body(updateRatingRequest)
            .when()
            .put(ControllerRouteConstant.UPDATE_RATING_URL.formatted(ratingId))
            .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", equalTo(ratingId.intValue()))
            .body("rating", equalTo(updateRatingRequest.rating().floatValue()))
            .body("comment", equalTo(updateRatingRequest.comment()));
    }

}
