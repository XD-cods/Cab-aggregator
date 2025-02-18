package com.vlad.kuzhyr.ratingservice.service.impl;

import com.vlad.kuzhyr.ratingservice.exception.RatingAlreadyExistsException;
import com.vlad.kuzhyr.ratingservice.exception.RatingNotFoundException;
import com.vlad.kuzhyr.ratingservice.exception.RatingsNotFoundedByDriverIdException;
import com.vlad.kuzhyr.ratingservice.exception.RatingsNotFoundedByPassengerIdException;
import com.vlad.kuzhyr.ratingservice.exception.RideInfoNotFoundException;
import com.vlad.kuzhyr.ratingservice.persistence.entity.RatedBy;
import com.vlad.kuzhyr.ratingservice.persistence.entity.Rating;
import com.vlad.kuzhyr.ratingservice.persistence.entity.RideInfo;
import com.vlad.kuzhyr.ratingservice.persistence.repository.RatingRepository;
import com.vlad.kuzhyr.ratingservice.persistence.repository.RideInfoRepository;
import com.vlad.kuzhyr.ratingservice.service.RatingService;
import com.vlad.kuzhyr.ratingservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.ratingservice.utility.mapper.PageResponseMapper;
import com.vlad.kuzhyr.ratingservice.utility.mapper.RatingMapper;
import com.vlad.kuzhyr.ratingservice.web.request.CreateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.request.UpdateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.response.AverageRatingResponse;
import com.vlad.kuzhyr.ratingservice.web.response.PageResponse;
import com.vlad.kuzhyr.ratingservice.web.response.RatingResponse;
import java.util.List;
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
    private final RideInfoRepository rideInfoRepository;

    @Value("${rating.last.rides.count:10}")
    private int lastRidesLimit;

    @Override
    public RatingResponse getRatingByRatingId(Long id) {
        Rating existingRating = getExistingRating(id);
        return ratingMapper.toResponse(existingRating);
    }

    @Override
    public PageResponse<RatingResponse> getRatings(int currentPage, int limit) {
        Pageable pageable = PageRequest.of(currentPage, limit, Sort.by(Sort.Order.desc("id")));
        Page<Rating> ratingsPage = ratingRepository.findAll(pageable);

        return pageResponseMapper.toPageResponse(
            ratingsPage,
            currentPage,
            ratingMapper::toResponse
        );
    }

    @Override
    public AverageRatingResponse getAverageRatingByPassengerId(Long passengerId) {
        List<Rating> lastRatings = getRatingsByPassengerId(passengerId);

        double averageRating = lastRatings.stream()
            .mapToDouble(Rating::getRating)
            .average()
            .orElse(0.0);

        AverageRatingResponse averageRatingResponse = AverageRatingResponse.builder()
            .averageRating(averageRating)
            .build();

        return averageRatingResponse;
    }

    @Override
    public AverageRatingResponse getAverageRatingByDriverId(Long driverId) {
        List<Rating> lastRatings = getRatingsByDriverId(driverId);

        double averageRating = lastRatings.stream()
            .mapToDouble(Rating::getRating)
            .average()
            .orElse(0.0);

        AverageRatingResponse averageRatingResponse = AverageRatingResponse.builder()
            .averageRating(averageRating)
            .build();

        return averageRatingResponse;
    }

    @Override
    public RatingResponse createRating(CreateRatingRequest createRatingRequest) {
        Long requestRideId = createRatingRequest.rideId();
        RatedBy requestRatedBy = createRatingRequest.ratedBy();

        validateCreateRating(requestRideId, requestRatedBy);

        RideInfo existingRideInfo = getRideInfoByRideId(requestRideId);
        Rating rating = ratingMapper.toEntity(createRatingRequest);
        rating.setRideInfo(existingRideInfo);

        existingRideInfo.getRating().add(rating);
        Rating savedRating = ratingRepository.save(rating);

        return ratingMapper.toResponse(savedRating);
    }

    @Override
    public RatingResponse updateRating(Long id, UpdateRatingRequest updateRatingRequest) {
        Rating existingRating = getExistingRating(id);

        ratingMapper.updateFromRequest(updateRatingRequest, existingRating);
        Rating savedRating = ratingRepository.save(existingRating);

        return ratingMapper.toResponse(savedRating);
    }

    private Rating getExistingRating(Long id) {
        return ratingRepository.findById(id)
            .orElseThrow(() -> new RatingNotFoundException(
                ExceptionMessageConstant.RATING_NOT_FOUND_MESSAGE.formatted(id)
            ));
    }

    private RideInfo getRideInfoByRideId(Long rideId) {
        return rideInfoRepository.findByRideId(rideId)
            .orElseThrow(() -> new RideInfoNotFoundException(
                ExceptionMessageConstant.RIDE_INFO_NOT_FOUND_MESSAGE.formatted(rideId)
            ));
    }

    private List<Rating> getRatingsByPassengerId(Long passengerId) {
        Pageable pageRequest = PageRequest.of(
            0,
            lastRidesLimit,
            Sort.by(Sort.Order.desc("id"))
        );

        List<Rating> ratings = ratingRepository.findByRideInfo_PassengerIdAndRatedBy(passengerId, pageRequest,
            RatedBy.DRIVER);

        if (ratings.isEmpty()) {
            throw new RatingsNotFoundedByPassengerIdException(
                ExceptionMessageConstant.RATINGS_NOT_FOUND_BY_PASSENGER_ID_MESSAGE.formatted(passengerId)
            );
        }
        return ratings;
    }

    private List<Rating> getRatingsByDriverId(Long driverId) {
        Pageable pageRequest = PageRequest.of(
            0,
            lastRidesLimit,
            Sort.by(Sort.Order.desc("id"))
        );

        List<Rating> ratings = ratingRepository.findByRideInfo_DriverIdAndRatedBy(driverId, pageRequest,
            RatedBy.PASSENGER);

        if (ratings.isEmpty()) {
            throw new RatingsNotFoundedByDriverIdException(
                ExceptionMessageConstant.RATINGS_NOT_FOUNDED_BY_DRIVER_ID_MESSAGE.formatted(driverId)
            );
        }

        return ratings;
    }

    private void validateCreateRating(Long requestRideId, RatedBy requestRatedBy) {
        if (ratingRepository.existsByRideInfo_RideIdAndRatedBy(
            requestRideId, requestRatedBy)) {
            throw new RatingAlreadyExistsException(
                ExceptionMessageConstant.RATING_ALREADY_EXISTS_MESSAGE.formatted(
                    requestRatedBy, requestRideId
                )
            );
        }
    }
}