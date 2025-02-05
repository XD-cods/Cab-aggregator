package com.vlad.kuzhyr.ratingservice.service.impl;

import com.vlad.kuzhyr.ratingservice.exception.RatingAlreadyExistsException;
import com.vlad.kuzhyr.ratingservice.exception.RatingNotFoundException;
import com.vlad.kuzhyr.ratingservice.exception.RatingsNotFoundedByDriverId;
import com.vlad.kuzhyr.ratingservice.exception.RatingsNotFoundedByPassengerId;
import com.vlad.kuzhyr.ratingservice.persistence.entity.Rating;
import com.vlad.kuzhyr.ratingservice.persistence.repository.RatingRepository;
import com.vlad.kuzhyr.ratingservice.service.RatingService;
import com.vlad.kuzhyr.ratingservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.ratingservice.utility.mapper.PageResponseMapper;
import com.vlad.kuzhyr.ratingservice.utility.mapper.RatingMapper;
import com.vlad.kuzhyr.ratingservice.web.request.RatingRequest;
import com.vlad.kuzhyr.ratingservice.web.response.PageResponse;
import com.vlad.kuzhyr.ratingservice.web.response.RatingResponse;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingMapper ratingMapper;

    private final RatingRepository ratingRepository;

    private final PageResponseMapper pageResponseMapper;

    @Value("${rating.last.rides.count:10}")
    private int lastRidesLimit;

    @Override
    public RatingResponse getRatingByRatingId(Long id) {
        Rating existingRating = getExistingRating(id);
        return ratingMapper.toResponse(existingRating);
    }

    @Override
    public PageResponse<RatingResponse> getRatings(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Rating> ratingsPage = ratingRepository.findAllRatings(pageable);

        return pageResponseMapper.toPageResponse(
            ratingsPage,
            offset,
            ratingMapper::toResponse
        );
    }

    @Override
    public Float getAverageRatingByPassengerId(Long passengerId) {
        Sort sort = Sort.by(Sort.Order.desc("ratingId"));
        Pageable pageRequest = PageRequest.of(0, lastRidesLimit, sort);

        List<Rating> lastRatings =
            ratingRepository.findAllByPassengerId(passengerId, pageRequest);

        if (lastRatings.isEmpty()) {
            throw new RatingsNotFoundedByPassengerId(
                ExceptionMessageConstant.RATINGS_NOT_FOUNDED_BY_PASSENGER_ID_MESSAGE.formatted(passengerId)
            );
        }

        float totalRating = (float) lastRatings.stream()
            .mapToDouble(Rating::getRating)
            .sum();

        return totalRating / lastRatings.size();
    }

    @Override
    public Float getAverageRatingByDriverId(Long driverId) {
        Sort sort = Sort.by(Sort.Order.desc("ratingId"));
        Pageable pageRequest = PageRequest.of(0, lastRidesLimit, sort);

        List<Rating> lastRatings = ratingRepository.findAllByDriverId(driverId, pageRequest);

        if (lastRatings.isEmpty()) {
            throw new RatingsNotFoundedByDriverId(
                ExceptionMessageConstant.RATINGS_NOT_FOUNDED_BY_DRIVER_ID_MESSAGE.formatted(driverId)
            );
        }

        float totalRating = (float) lastRatings.stream()
            .mapToDouble(Rating::getRating)
            .sum();

        return totalRating / lastRatings.size();
    }

    @Override
    public RatingResponse createRating(RatingRequest ratingRequest) {
        Optional<Rating> existingRating = ratingRepository.findByRideIdAndRatedBy(
            ratingRequest.rideId(), ratingRequest.ratedBy()
        );

        if (existingRating.isPresent()) {
            throw new RatingAlreadyExistsException(
                ExceptionMessageConstant.RATING_ALREADY_EXISTS_MESSAGE.formatted(
                    ratingRequest.ratedBy(),
                    ratingRequest.rideId()
                )
            );
        }

        Rating rating = ratingMapper.toEntity(ratingRequest);
        rating = ratingRepository.save(rating);

        return ratingMapper.toResponse(rating);
    }

    @Override
    public RatingResponse updateRating(Long id, RatingRequest ratingRequest) {
        return null;
    }

    private Rating getExistingRating(Long id) {
        return ratingRepository.findById(id)
            .orElseThrow(() -> new RatingNotFoundException(
                ExceptionMessageConstant.RATING_NOT_FOUND_MESSAGE.formatted(id)
            ));
    }

}
