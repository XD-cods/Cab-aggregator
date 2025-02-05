package com.vlad.kuzhyr.ratingservice.service;

import com.vlad.kuzhyr.ratingservice.web.request.RatingRequest;
import com.vlad.kuzhyr.ratingservice.web.response.PageResponse;
import com.vlad.kuzhyr.ratingservice.web.response.RatingResponse;

public interface RatingService {

    RatingResponse getRatingByRatingId(Long id);

    PageResponse<RatingResponse> getRatings(int offset, int limit);

    Float getAverageRatingByPassengerId(Long passengerId);

    Float getAverageRatingByDriverId(Long driverId);

    RatingResponse createRating(RatingRequest ratingRequest);

    RatingResponse updateRating(Long id, RatingRequest ratingRequest);

}
