package com.vlad.kuzhyr.ratingservice.web.controller.ipml;

import com.vlad.kuzhyr.ratingservice.persistence.entity.Rating;
import com.vlad.kuzhyr.ratingservice.web.controller.RatingController;
import com.vlad.kuzhyr.ratingservice.web.request.RatingRequest;
import com.vlad.kuzhyr.ratingservice.web.response.PageResponse;
import com.vlad.kuzhyr.ratingservice.web.response.RatingResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
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

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<RatingResponse> getRatingByRatingId(@PathVariable Long id) {
    return null;
  }

  @Override
  @GetMapping
  public ResponseEntity<PageResponse<Rating>> getRatings(
          @RequestParam(required = false, defaultValue = "0") @Min(0) int offset,
          @RequestParam(required = false, defaultValue = "10") @Min(0) @Max(100) int limit
  ) {
    return null;
  }

  @Override
  @PostMapping
  public ResponseEntity<RatingResponse> createRating(RatingRequest ratingRequest) {
    return null;
  }

  @Override
  @PutMapping("/{id}")
  public ResponseEntity<RatingResponse> updateRating(
          @PathVariable Long id,
          @Valid @RequestBody RatingRequest ratingRequest
  ) {
    return null;
  }

}
