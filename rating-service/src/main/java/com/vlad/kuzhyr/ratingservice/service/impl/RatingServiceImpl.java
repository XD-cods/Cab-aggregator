package com.vlad.kuzhyr.ratingservice.service.impl;

import com.vlad.kuzhyr.ratingservice.exception.RatingNotFoundException;
import com.vlad.kuzhyr.ratingservice.persistence.entity.Rating;
import com.vlad.kuzhyr.ratingservice.persistence.repository.RatingRepository;
import com.vlad.kuzhyr.ratingservice.service.RatingService;
import com.vlad.kuzhyr.ratingservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.ratingservice.utility.mapper.PageResponseMapper;
import com.vlad.kuzhyr.ratingservice.utility.mapper.RatingMapper;
import com.vlad.kuzhyr.ratingservice.web.request.RatingRequest;
import com.vlad.kuzhyr.ratingservice.web.response.PageResponse;
import com.vlad.kuzhyr.ratingservice.web.response.RatingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingMapper ratingMapper;

    private final RatingRepository ratingRepository;

    private final PageResponseMapper pageResponseMapper;

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
    public RatingResponse getAverageRatingByPassengerIdAndCountLastRides(Long passengerId, int limitLastRides) {
        return null;
    }

    @Override
    public RatingResponse getAverageRatingByDriverIdAndCountLastRides(Long driverId, int limitLastRides) {
        return null;
    }

    @Override
    public RatingResponse createRating(RatingRequest ratingRequest) {
        return null;
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
