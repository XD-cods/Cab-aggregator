package com.vlad.kuzhyr.ratingservice.service;

import com.vlad.kuzhyr.ratingservice.web.dto.request.CreateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.dto.request.UpdateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.dto.response.AverageRatingResponse;
import com.vlad.kuzhyr.ratingservice.web.dto.response.PageResponse;
import com.vlad.kuzhyr.ratingservice.web.dto.response.RatingResponse;

public interface RatingService {

    RatingResponse getRatingByRatingId(Long id);

    PageResponse<RatingResponse> getRatings(int currentPage, int limit);

    AverageRatingResponse getAverageRatingByPassengerId(Long passengerId);

    AverageRatingResponse getAverageRatingByDriverId(Long driverId);

    RatingResponse createRating(CreateRatingRequest createRatingRequest);

    RatingResponse updateRating(Long id, UpdateRatingRequest updateRatingRequest);

}
