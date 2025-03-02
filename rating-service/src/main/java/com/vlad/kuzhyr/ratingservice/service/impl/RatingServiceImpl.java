package com.vlad.kuzhyr.ratingservice.service.impl;

import com.vlad.kuzhyr.ratingservice.exception.RatingNotFoundException;
import com.vlad.kuzhyr.ratingservice.exception.RatingsNotFoundedByDriverIdException;
import com.vlad.kuzhyr.ratingservice.exception.RatingsNotFoundedByPassengerIdException;
import com.vlad.kuzhyr.ratingservice.persistence.entity.RatedBy;
import com.vlad.kuzhyr.ratingservice.persistence.entity.Rating;
import com.vlad.kuzhyr.ratingservice.persistence.entity.RideInfo;
import com.vlad.kuzhyr.ratingservice.persistence.repository.RatingRepository;
import com.vlad.kuzhyr.ratingservice.service.RatingService;
import com.vlad.kuzhyr.ratingservice.service.RideInfoService;
import com.vlad.kuzhyr.ratingservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.ratingservice.utility.mapper.PageResponseMapper;
import com.vlad.kuzhyr.ratingservice.utility.mapper.RatingMapper;
import com.vlad.kuzhyr.ratingservice.utility.validator.RatingValidator;
import com.vlad.kuzhyr.ratingservice.web.dto.request.CreateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.dto.request.UpdateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.dto.response.AverageRatingResponse;
import com.vlad.kuzhyr.ratingservice.web.dto.response.PageResponse;
import com.vlad.kuzhyr.ratingservice.web.dto.response.RatingResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingMapper ratingMapper;
    private final RatingRepository ratingRepository;
    private final PageResponseMapper pageResponseMapper;
    private final RideInfoService rideInfoService;
    private final RatingValidator ratingValidator;

    @Value("${rating.last.rides.count:10}")
    private int lastRidesLimit;

    @Override
    public RatingResponse getRatingByRatingId(Long id) {
        log.debug("getRatingByRatingId: Entering method. Rating id: {}", id);

        Rating existingRating = getExistingRating(id);

        log.info("getRatingByRatingId: Rating found. Rating id: {}", id);
        return ratingMapper.toResponse(existingRating);
    }

    @Override
    public PageResponse<RatingResponse> getRatings(int currentPage, int limit) {
        log.debug("getRatings: Entering method. Current page: {}, limit: {}", currentPage, limit);

        Pageable pageable = PageRequest.of(currentPage, limit, Sort.by(Sort.Order.desc("id")));
        Page<Rating> ratingsPage = ratingRepository.findAll(pageable);

        PageResponse<RatingResponse> pageResponse = pageResponseMapper.toPageResponse(
            ratingsPage,
            currentPage,
            ratingMapper::toResponse
        );

        log.info("getRatings: Page of ratings retrieved. {}", pageResponse);
        return pageResponse;
    }

    @Override
    public AverageRatingResponse getAverageRatingByPassengerId(Long passengerId) {
        log.debug("getAverageRatingByPassengerId: Entering method. Passenger id: {}", passengerId);

        List<Rating> lastRatings = getRatingsByPassengerId(passengerId);

        AverageRatingResponse averageRatingResponse = getAverageRatingResponse(lastRatings);

        log.info("getAverageRatingByPassengerId: Average rating calculated. Passenger id: {}, {}",
            passengerId,
            averageRatingResponse.averageRating()
        );
        return averageRatingResponse;
    }

    @Override
    public AverageRatingResponse getAverageRatingByDriverId(Long driverId) {
        log.debug("getAverageRatingByDriverId: Entering method. Driver id: {}", driverId);

        List<Rating> lastRatings = getRatingsByDriverId(driverId);

        AverageRatingResponse averageRatingResponse = getAverageRatingResponse(lastRatings);

        log.info("getAverageRatingByDriverId: Average rating calculated. Driver id: {}, {}",
            driverId,
            averageRatingResponse.averageRating()
        );
        return averageRatingResponse;
    }

    @Override
    @Transactional
    public RatingResponse createRating(CreateRatingRequest createRatingRequest) {
        log.debug("createRating: Entering method. {}", createRatingRequest);

        Long requestRideId = createRatingRequest.rideId();
        RatedBy requestRatedBy = createRatingRequest.ratedBy();

        ratingValidator.validateCreateRating(requestRideId, requestRatedBy);

        RideInfo existingRideInfo = rideInfoService.getRideInfoByRideId(requestRideId);
        Rating rating = ratingMapper.toEntity(createRatingRequest);
        rating.setRideInfo(existingRideInfo);

        existingRideInfo.getRating().add(rating);
        Rating savedRating = ratingRepository.save(rating);

        log.info("createRating: Rating created successfully. Rating id: {}", savedRating.getId());
        return ratingMapper.toResponse(savedRating);
    }

    @Override
    @Transactional
    public RatingResponse updateRating(Long id, UpdateRatingRequest updateRatingRequest) {
        log.debug("updateRating: Entering method. Rating id: {}, {}", id, updateRatingRequest);

        Rating existingRating = getExistingRating(id);

        ratingMapper.updateFromRequest(updateRatingRequest, existingRating);
        Rating savedRating = ratingRepository.save(existingRating);

        log.info("updateRating: Rating updated successfully. Rating id: {}", savedRating.getId());
        return ratingMapper.toResponse(savedRating);
    }

    private Rating getExistingRating(Long id) {
        log.debug("getExistingRating: Attempting to find rating. Rating id: {}", id);

        return ratingRepository.findById(id)
            .orElseThrow(() -> {
                log.error("getExistingRating: Rating not found. Rating id: {}", id);
                return new RatingNotFoundException(
                    ExceptionMessageConstant.RATING_NOT_FOUND_MESSAGE.formatted(id)
                );
            });
    }

    private List<Rating> getRatingsByPassengerId(Long passengerId) {
        log.debug("getRatingsByPassengerId: Attempting to find ratings by passenger id. Passenger id: {}", passengerId);

        Pageable pageRequest = PageRequest.of(
            0,
            lastRidesLimit,
            Sort.by(Sort.Order.desc("id"))
        );

        List<Rating> ratings = ratingRepository.findByRideInfo_PassengerIdAndRatedBy(passengerId, pageRequest,
            RatedBy.DRIVER);

        if (ratings.isEmpty()) {
            log.error("getRatingsByPassengerId: Ratings not found by passenger id. Passenger id: {}", passengerId);
            throw new RatingsNotFoundedByPassengerIdException(
                ExceptionMessageConstant.RATINGS_NOT_FOUND_BY_PASSENGER_ID_MESSAGE.formatted(passengerId)
            );
        }
        return ratings;
    }

    private List<Rating> getRatingsByDriverId(Long driverId) {
        log.debug("getRatingsByDriverId: Attempting to find ratings by driver id. Driver id: {}", driverId);

        Pageable pageRequest = PageRequest.of(
            0,
            lastRidesLimit,
            Sort.by(Sort.Order.desc("id"))
        );

        List<Rating> ratings = ratingRepository.findByRideInfo_DriverIdAndRatedBy(driverId, pageRequest,
            RatedBy.PASSENGER);

        if (ratings.isEmpty()) {
            log.error("getRatingsByDriverId: Ratings not found by driver id. Driver id: {}", driverId);
            throw new RatingsNotFoundedByDriverIdException(
                ExceptionMessageConstant.RATINGS_NOT_FOUNDED_BY_DRIVER_ID_MESSAGE.formatted(driverId)
            );
        }

        return ratings;
    }

    private AverageRatingResponse getAverageRatingResponse(List<Rating> lastRatings) {
        log.debug("getAverageRatingResponse: Calculating average rating. Last ratings collection size: {}",
            lastRatings.size());

        double averageRating = lastRatings.stream()
            .mapToDouble(Rating::getRating)
            .average()
            .orElse(0.0);

        AverageRatingResponse averageRatingResponse = AverageRatingResponse.builder()
            .averageRating(averageRating)
            .build();

        log.debug("getAverageRatingResponse: Average rating calculated. Average rating: {}",
            averageRatingResponse.averageRating());
        return averageRatingResponse;
    }
}
