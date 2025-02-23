package com.vlad.kuzhyr.ratingservice.service;

import com.vlad.kuzhyr.ratingservice.web.request.CreateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.request.UpdateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.response.AverageRatingResponse;
import com.vlad.kuzhyr.ratingservice.web.response.PageResponse;
import com.vlad.kuzhyr.ratingservice.web.response.RatingResponse;

public interface RatingService {

    RatingResponse getRatingByRatingId(Long id);

    PageResponse<RatingResponse> getRatings(int currentPage, int limit);

    AverageRatingResponse getAverageRatingByPassengerId(Long passengerId);

    AverageRatingResponse getAverageRatingByDriverId(Long driverId);

    RatingResponse createRating(CreateRatingRequest createRatingRequest);

    RatingResponse updateRating(Long id, UpdateRatingRequest updateRatingRequest);

}
