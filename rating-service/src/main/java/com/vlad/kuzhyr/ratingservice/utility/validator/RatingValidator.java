package com.vlad.kuzhyr.ratingservice.utility.validator;

import com.vlad.kuzhyr.ratingservice.exception.RatingAlreadyExistsException;
import com.vlad.kuzhyr.ratingservice.persistence.entity.RatedBy;
import com.vlad.kuzhyr.ratingservice.persistence.repository.RatingRepository;
import com.vlad.kuzhyr.ratingservice.utility.constant.ExceptionMessageConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RatingValidator {

    private final RatingRepository ratingRepository;

    public void validateCreateRating(Long requestRideId, RatedBy requestRatedBy) {
        log.debug("Rating validator. Validate create rating. Ride id: {}, rated by: {}",
            requestRideId,
            requestRatedBy
        );

        if (ratingRepository.existsByRideInfo_RideIdAndRatedBy(
            requestRideId, requestRatedBy)) {
            log.error("Rating validator. Rating already exists. Ride id: {}, rated by: {}",
                requestRideId,
                requestRatedBy
            );
            throw new RatingAlreadyExistsException(
                ExceptionMessageConstant.RATING_ALREADY_EXISTS_MESSAGE.formatted(
                    requestRatedBy, requestRideId
                )
            );
        }
    }

}
