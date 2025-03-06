package com.vlad.kuzhyr.ratingservice.constant;

import com.vlad.kuzhyr.ratingservice.persistence.entity.RatedBy;
import com.vlad.kuzhyr.ratingservice.persistence.entity.Rating;
import com.vlad.kuzhyr.ratingservice.persistence.entity.RideInfo;
import com.vlad.kuzhyr.ratingservice.web.dto.request.CreateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.dto.request.UpdateRatingRequest;

public final class IntegrationTestDataProvider {

    public static final Long BASE_RIDE_ID = 1L;
    public static final Long BASE_DRIVER_ID = 1L;
    public static final Long BASE_PASSENGER_ID = 1L;
    public static final Double BASE_RATING = 4.5;
    public static final String BASE_COMMENT = "This ride was great!";
    public static final RatedBy BASE_RATED_BY = RatedBy.PASSENGER;

    public static RideInfo createRideInfo() {
        return RideInfo.builder()
            .rideId(BASE_RIDE_ID)
            .driverId(BASE_DRIVER_ID)
            .passengerId(BASE_PASSENGER_ID)
            .build();
    }

    public static RideInfo createRideInfo2() {
        return RideInfo.builder()
            .rideId(2L)
            .driverId(2L)
            .passengerId(2L)
            .build();
    }

    public static Rating createRating(RideInfo rideInfo) {
        return Rating.builder()
            .rideInfo(rideInfo)
            .rating(BASE_RATING)
            .comment(BASE_COMMENT)
            .ratedBy(BASE_RATED_BY)
            .build();
    }

    public static CreateRatingRequest createRatingRequest(Long rideId) {
        return CreateRatingRequest.builder()
            .rideId(rideId)
            .rating(BASE_RATING)
            .comment(BASE_COMMENT)
            .ratedBy(BASE_RATED_BY)
            .build();
    }

    public static CreateRatingRequest invalidCreateRatingRequest() {
        return CreateRatingRequest.builder()
            .rideId(null)
            .rating(5.0)
            .comment(null)
            .ratedBy(null)
            .build();

    }

    public static UpdateRatingRequest updateRatingRequest() {
        return UpdateRatingRequest.builder()
            .rating(3.0)
            .comment("This rating was great! But was updated by Vlad")
            .build();
    }
}