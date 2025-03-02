package com.vlad.kuzhyr.ratingservice.utility.validator.unittest;

import com.vlad.kuzhyr.ratingservice.exception.RatingAlreadyExistsException;
import com.vlad.kuzhyr.ratingservice.persistence.entity.RatedBy;
import com.vlad.kuzhyr.ratingservice.persistence.repository.RatingRepository;
import com.vlad.kuzhyr.ratingservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.ratingservice.utility.validator.RatingValidator;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RatingValidatorTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingValidator ratingValidator;

    @ParameterizedTest
    @CsvSource({
        "1, PASSENGER",
        "1, DRIVER"
    })
    void validateCreateRating_shouldNotThrowException(Long rideId, RatedBy ratedBy) {

        when(ratingRepository.existsByRideInfo_RideIdAndRatedBy(rideId, ratedBy)).thenReturn(false);

        assertDoesNotThrow(() -> ratingValidator.validateCreateRating(rideId, ratedBy));

        verify(ratingRepository).existsByRideInfo_RideIdAndRatedBy(rideId, ratedBy);
    }

    @ParameterizedTest
    @CsvSource({
        "1, PASSENGER",
        "1, DRIVER"
    })
    void validateCreateRating_shouldThrowRatingAlreadyExistsException(Long rideId, RatedBy ratedBy) {
        when(ratingRepository.existsByRideInfo_RideIdAndRatedBy(rideId, ratedBy)).thenReturn(true);

        RatingAlreadyExistsException exception = assertThrows(
            RatingAlreadyExistsException.class,
            () -> ratingValidator.validateCreateRating(rideId, ratedBy)
        );

        assertEquals(
            ExceptionMessageConstant.RATING_ALREADY_EXISTS_MESSAGE.formatted(ratedBy, rideId),
            exception.getMessage()
        );

        verify(ratingRepository).existsByRideInfo_RideIdAndRatedBy(rideId, ratedBy);
    }
}