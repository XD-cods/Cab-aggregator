package com.vlad.kuzhyr.ratingservice.utility.validator;

import com.vlad.kuzhyr.ratingservice.exception.RatingAlreadyExistsException;
import com.vlad.kuzhyr.ratingservice.persistence.entity.RatedBy;
import com.vlad.kuzhyr.ratingservice.persistence.repository.RatingRepository;
import com.vlad.kuzhyr.ratingservice.utility.constant.ExceptionMessageConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RatingValidator {

    private final RatingRepository ratingRepository;

    public void validateCreateRating(Long requestRideId, RatedBy requestRatedBy) {
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
