package com.vlad.kuzhyr.ratingservice.web.controller;

import com.vlad.kuzhyr.ratingservice.web.request.CreateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.request.UpdateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.response.PageResponse;
import com.vlad.kuzhyr.ratingservice.web.response.RatingResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Rating API", description = "API for managing rating")
public interface RatingController {

    @Operation(summary = "Get rating by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rating founded"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<RatingResponse> getRatingByRatingId(Long id);

    @Operation(summary = "Get all ratings")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ratings founded"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<PageResponse<RatingResponse>> getRatings(
        @RequestParam(required = false, defaultValue = "0") @Min(0) int currentPage,
        @RequestParam(required = false, defaultValue = "10") @Min(0) @Max(100) int limit
    );

    @Operation(summary = "Get average rating by passenger id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ratings founded"),
        @ApiResponse(responseCode = "404", description = "Ratings not founded"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<Double> getAverageRatingByPassengerId(Long passengerId);

    @Operation(summary = "Get average rating by driver id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ratings founded"),
        @ApiResponse(responseCode = "404", description = "Ratings not founded"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<Double> getAverageRatingByDriverId(Long driverId);

    @Operation(summary = "Create rating")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rating created"),
        @ApiResponse(responseCode = "404", description = "Rating not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<RatingResponse> createRating(CreateRatingRequest createRatingRequest);

    @Operation(summary = "Update rating by id and rating request")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rating updated"),
        @ApiResponse(responseCode = "404", description = "Rating not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<RatingResponse> updateRating(Long id, UpdateRatingRequest updateRatingRequest);

}
