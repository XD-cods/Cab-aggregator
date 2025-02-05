package com.vlad.kuzhyr.ratingservice.service;

import com.vlad.kuzhyr.ratingservice.web.request.RatingRequest;
import com.vlad.kuzhyr.ratingservice.web.response.PageResponse;
import com.vlad.kuzhyr.ratingservice.web.response.RatingResponse;

public interface RatingService {

    RatingResponse getRatingByRatingId(Long id);

    PageResponse<RatingResponse> getRatings(int offset, int limit);

    RatingResponse getAverageRatingByPassengerIdAndCountLastRides(Long passengerId, int limitLastRides);

    RatingResponse getAverageRatingByDriverIdAndCountLastRides(Long driverId, int limitLastRides);

    RatingResponse createRating(RatingRequest ratingRequest);

    RatingResponse updateRating(Long id, RatingRequest ratingRequest);

}
