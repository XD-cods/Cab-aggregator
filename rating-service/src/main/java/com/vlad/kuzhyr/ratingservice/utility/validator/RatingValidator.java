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
        log.debug("validateCreateRating: Validate rating creation by ride id and ratedBy. Ride id: {}, ratedBy: {}",
            requestRideId,
            requestRatedBy
        );

        if (ratingRepository.existsByRideInfo_RideIdAndRatedBy(
            requestRideId, requestRatedBy)) {
            log.error("validateCreateRating: Rating already exists. Ride id: {}, rated by: {}",
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
