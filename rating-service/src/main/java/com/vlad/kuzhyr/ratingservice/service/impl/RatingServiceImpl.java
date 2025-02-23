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
        Rating existingRating = getExistingRating(id);

        log.info("Rating service. Get rating by id. Rating id: {}", id);
        return ratingMapper.toResponse(existingRating);
    }

    @Override
    public PageResponse<RatingResponse> getRatings(int currentPage, int limit) {
        Pageable pageable = PageRequest.of(currentPage, limit, Sort.by(Sort.Order.desc("id")));
        Page<Rating> ratingsPage = ratingRepository.findAll(pageable);

        PageResponse<RatingResponse> pageResponse = pageResponseMapper.toPageResponse(
            ratingsPage,
            currentPage,
            ratingMapper::toResponse
        );

        log.info("Rating service. Fetch all ratings. Current page: {}, total pages: {}",
            pageResponse.currentPage(),
            pageResponse.totalPages()
        );
        return pageResponse;
    }

    @Override
    public AverageRatingResponse getAverageRatingByPassengerId(Long passengerId) {
        List<Rating> lastRatings = getRatingsByPassengerId(passengerId);

        AverageRatingResponse averageRatingResponse = getAverageRatingResponse(lastRatings);

        log.info("Rating service. Get average rating for passenger. Passenger id: {}, average rating: {}",
            passengerId,
            averageRatingResponse.averageRating()
        );
        return averageRatingResponse;
    }

    @Override
    public AverageRatingResponse getAverageRatingByDriverId(Long driverId) {
        List<Rating> lastRatings = getRatingsByDriverId(driverId);

        AverageRatingResponse averageRatingResponse = getAverageRatingResponse(lastRatings);

        log.info("Rating service. Get average rating for driver. Driver id: {}, average rating: {}",
            driverId,
            averageRatingResponse.averageRating()
        );
        return averageRatingResponse;
    }

    @Override
    public RatingResponse createRating(CreateRatingRequest createRatingRequest) {
        Long requestRideId = createRatingRequest.rideId();
        RatedBy requestRatedBy = createRatingRequest.ratedBy();

        log.debug("Rating service. Create rating. Ride id: {}, rated by: {}",
            requestRideId,
            requestRatedBy
        );

        ratingValidator.validateCreateRating(requestRideId, requestRatedBy);

        RideInfo existingRideInfo = rideInfoService.getRideInfoByRideId(requestRideId);
        Rating rating = ratingMapper.toEntity(createRatingRequest);
        rating.setRideInfo(existingRideInfo);

        existingRideInfo.getRating().add(rating);
        Rating savedRating = ratingRepository.save(rating);

        log.info("Rating service. Created new rating. Rating id: {}", savedRating.getId());
        return ratingMapper.toResponse(savedRating);
    }

    @Override
    public RatingResponse updateRating(Long id, UpdateRatingRequest updateRatingRequest) {
        Rating existingRating = getExistingRating(id);

        log.debug("Rating service. Update rating. Rating id: {}", id);

        ratingMapper.updateFromRequest(updateRatingRequest, existingRating);
        Rating savedRating = ratingRepository.save(existingRating);

        log.info("Rating service. Updated rating. Rating id: {}", savedRating.getId());
        return ratingMapper.toResponse(savedRating);
    }

    private Rating getExistingRating(Long id) {
        log.debug("Rating service. Attempting to find rating. Rating id: {}", id);

        return ratingRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Rating service. Rating not found. Rating id: {}", id);
                return new RatingNotFoundException(
                    ExceptionMessageConstant.RATING_NOT_FOUND_MESSAGE.formatted(id)
                );
            });
    }

    private List<Rating> getRatingsByPassengerId(Long passengerId) {
        log.debug("Rating service. Attempting to find ratings by passenger id. Passenger id: {}", passengerId);

        Pageable pageRequest = PageRequest.of(
            0,
            lastRidesLimit,
            Sort.by(Sort.Order.desc("id"))
        );

        List<Rating> ratings = ratingRepository.findByRideInfo_PassengerIdAndRatedBy(passengerId, pageRequest,
            RatedBy.DRIVER);

        if (ratings.isEmpty()) {
            log.error("Rating service. Not found ratings by passenger id. Passenger id: {}", passengerId);

            throw new RatingsNotFoundedByPassengerIdException(
                ExceptionMessageConstant.RATINGS_NOT_FOUND_BY_PASSENGER_ID_MESSAGE.formatted(passengerId)
            );
        }
        return ratings;
    }

    private List<Rating> getRatingsByDriverId(Long driverId) {
        log.debug("Rating service. Attempting to find ratings by driver id. Driver id: {}", driverId);

        Pageable pageRequest = PageRequest.of(
            0,
            lastRidesLimit,
            Sort.by(Sort.Order.desc("id"))
        );

        List<Rating> ratings = ratingRepository.findByRideInfo_DriverIdAndRatedBy(driverId, pageRequest,
            RatedBy.PASSENGER);

        if (ratings.isEmpty()) {
            log.error("Rating service. Not found ratings by driver id. Driver id: {}", driverId);
            throw new RatingsNotFoundedByDriverIdException(
                ExceptionMessageConstant.RATINGS_NOT_FOUNDED_BY_DRIVER_ID_MESSAGE.formatted(driverId)
            );
        }

        return ratings;
    }


    private AverageRatingResponse getAverageRatingResponse(List<Rating> lastRatings) {
        log.debug("Rating service. Get average rating response. Last ratings collection size: {}", lastRatings.size());

        double averageRating = lastRatings.stream()
            .mapToDouble(Rating::getRating)
            .average()
            .orElse(0.0);

        AverageRatingResponse averageRatingResponse = AverageRatingResponse.builder()
            .averageRating(averageRating)
            .build();

        log.debug("Rating service. Get average rating response. Average rating: {}",
            averageRatingResponse.averageRating());
        return averageRatingResponse;
    }

}