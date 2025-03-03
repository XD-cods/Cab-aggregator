package com.vlad.kuzhyr.ratingservice.constant;

import com.vlad.kuzhyr.ratingservice.persistence.entity.RatedBy;
import com.vlad.kuzhyr.ratingservice.persistence.entity.Rating;
import com.vlad.kuzhyr.ratingservice.persistence.entity.RideInfo;
import com.vlad.kuzhyr.ratingservice.web.dto.request.CreateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.dto.request.UpdateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.dto.response.AverageRatingResponse;
import com.vlad.kuzhyr.ratingservice.web.dto.response.RatingResponse;
import com.vlad.kuzhyr.ratingservice.web.dto.response.RideInfoResponse;

public class UnitTestDataProvider {

    public static final Long TEST_ID = 1L;
    public static final Long TEST_RIDE_ID = 1L;
    public static final Long TEST_PASSENGER_ID = 1L;
    public static final Long TEST_DRIVER_ID = 1L;
    public static final double TEST_RATING_VALUE = 4.5;
    public static final String TEST_COMMENT = "Great ride!";

    public static Rating rating() {
        return Rating.builder()
            .id(TEST_ID)
            .ratedBy(RatedBy.DRIVER)
            .rating(TEST_RATING_VALUE)
            .comment(TEST_COMMENT)
            .rideInfo(rideInfo())
            .build();
    }

    public static RideInfo rideInfo() {
        return RideInfo.builder()
            .rideInfoId(TEST_ID)
            .rideId(TEST_RIDE_ID)
            .driverId(TEST_DRIVER_ID)
            .passengerId(TEST_PASSENGER_ID)
            .build();
    }

    public static RideInfoResponse rideInfoResponse(){
        return RideInfoResponse.builder()
            .rideId(TEST_RIDE_ID)
            .driverId(TEST_DRIVER_ID)
            .passengerId(TEST_PASSENGER_ID)
            .build();
    }

    public static CreateRatingRequest createRatingRequest() {
        return CreateRatingRequest.builder()
            .rideId(TEST_RIDE_ID)
            .rating(TEST_RATING_VALUE)
            .comment(TEST_COMMENT)
            .ratedBy(RatedBy.DRIVER)
            .build();
    }

    public static UpdateRatingRequest updateRatingRequest() {
        return UpdateRatingRequest.builder()
            .rating(TEST_RATING_VALUE)
            .comment(TEST_COMMENT)
            .build();
    }

    public static RatingResponse ratingResponse() {
        return RatingResponse.builder()
            .id(TEST_ID)
            .rideInfo(rideInfoResponse())
            .rating(TEST_RATING_VALUE)
            .comment(TEST_COMMENT)
            .ratedBy(RatedBy.DRIVER)
            .build();
    }

    public static AverageRatingResponse averageRatingResponse() {
        return AverageRatingResponse.builder()
            .averageRating(TEST_RATING_VALUE)
            .build();
    }

}
