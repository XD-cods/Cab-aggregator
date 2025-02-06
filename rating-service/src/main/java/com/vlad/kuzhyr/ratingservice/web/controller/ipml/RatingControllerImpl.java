package com.vlad.kuzhyr.ratingservice.web.controller.ipml;

import com.vlad.kuzhyr.ratingservice.service.RatingService;
import com.vlad.kuzhyr.ratingservice.web.controller.RatingController;
import com.vlad.kuzhyr.ratingservice.web.request.CreateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.request.UpdateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.response.PageResponse;
import com.vlad.kuzhyr.ratingservice.web.response.RatingResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ratings")
public class RatingControllerImpl implements RatingController {

    private final RatingService ratingService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<RatingResponse> getRatingByRatingId(@PathVariable Long id) {
        return ResponseEntity.ok(ratingService.getRatingByRatingId(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<PageResponse<RatingResponse>> getRatings(
        @RequestParam(required = false, defaultValue = "0")  @Min(0) int currentPage,
        @RequestParam(required = false, defaultValue = "10") @Min(0) @Max(100) int limit
    ) {
        return ResponseEntity.ok(ratingService.getRatings(currentPage, limit));
    }

    @Override
    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<Double> getAverageRatingByPassengerId(@PathVariable Long passengerId) {
        return ResponseEntity.ok(
            ratingService.getAverageRatingByPassengerId(passengerId));
    }

    @Override
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<Double> getAverageRatingByDriverId(@PathVariable Long driverId) {
        return ResponseEntity.ok(ratingService.getAverageRatingByDriverId(driverId));
    }

    @Override
    @PostMapping
    public ResponseEntity<RatingResponse> createRating(@Valid @RequestBody CreateRatingRequest createRatingRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.createRating(createRatingRequest));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<RatingResponse> updateRating(
        @PathVariable Long id,
        @Valid @RequestBody UpdateRatingRequest updateRatingRequest
    ) {
        return ResponseEntity.ok(ratingService.updateRating(id, updateRatingRequest));
    }

}
