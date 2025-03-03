package com.vlad.kuzhyr.ratingservice.service.impl.unittest;

import com.vlad.kuzhyr.ratingservice.constant.UnitTestDataProvider;
import com.vlad.kuzhyr.ratingservice.exception.RatingNotFoundException;
import com.vlad.kuzhyr.ratingservice.exception.RatingsNotFoundedByDriverIdException;
import com.vlad.kuzhyr.ratingservice.exception.RatingsNotFoundedByPassengerIdException;
import com.vlad.kuzhyr.ratingservice.persistence.entity.RatedBy;
import com.vlad.kuzhyr.ratingservice.persistence.entity.Rating;
import com.vlad.kuzhyr.ratingservice.persistence.repository.RatingRepository;
import com.vlad.kuzhyr.ratingservice.service.RideInfoService;
import com.vlad.kuzhyr.ratingservice.service.impl.RatingServiceImpl;
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
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class RatingServiceImplTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RatingMapper ratingMapper;

    @Mock
    private PageResponseMapper pageResponseMapper;

    @Mock
    private RideInfoService rideInfoService;

    @Mock
    private RatingValidator ratingValidator;

    @InjectMocks
    private RatingServiceImpl ratingServiceImpl;

    private Rating rating;
    private RatingResponse ratingResponse;
    private CreateRatingRequest createRatingRequest;
    private UpdateRatingRequest updateRatingRequest;
    private AverageRatingResponse averageRatingResponse;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(ratingServiceImpl, "lastRidesLimit", 10);
        rating = UnitTestDataProvider.rating();
        ratingResponse = UnitTestDataProvider.ratingResponse();
        createRatingRequest = UnitTestDataProvider.createRatingRequest();
        updateRatingRequest = UnitTestDataProvider.updateRatingRequest();
        averageRatingResponse = UnitTestDataProvider.averageRatingResponse();
    }

    @Test
    void getRatingByRatingId_shouldReturnRatingResponse() {
        Long existingRatingId = rating.getId();

        when(ratingRepository.findById(existingRatingId)).thenReturn(Optional.of(rating));
        when(ratingMapper.toResponse(rating)).thenReturn(ratingResponse);

        RatingResponse result = ratingServiceImpl.getRatingByRatingId(existingRatingId);

        assertNotNull(result);
        assertEquals(ratingResponse, result);

        verify(ratingRepository).findById(existingRatingId);
        verify(ratingMapper).toResponse(rating);
    }

    @Test
    void getRatingByRatingId_shouldThrowNotFoundException() {
        Long nonExistingRatingId = 2L;

        when(ratingRepository.findById(nonExistingRatingId)).thenReturn(Optional.empty());

        RatingNotFoundException exception = assertThrows(
            RatingNotFoundException.class,
            () -> ratingServiceImpl.getRatingByRatingId(nonExistingRatingId)
        );

        assertEquals(ExceptionMessageConstant.RATING_NOT_FOUND_MESSAGE.formatted(nonExistingRatingId),
            exception.getMessage());

        verify(ratingRepository).findById(nonExistingRatingId);
        verifyNoInteractions(ratingMapper);
    }

    @Test
    void getRatings_shouldReturnPageResponse() {
        int currentPage = 0;
        int limit = 10;
        PageRequest pageRequest = PageRequest.of(currentPage, limit, Sort.by(Sort.Order.desc("id")));

        List<Rating> ratings = List.of(rating, rating, rating);
        Page<Rating> ratingPage = new PageImpl<>(ratings, pageRequest, ratings.size());

        when(ratingRepository.findAll(pageRequest)).thenReturn(ratingPage);

        when(pageResponseMapper.toPageResponse(eq(ratingPage), eq(currentPage), any()))
            .thenReturn(new PageResponse<>(
                List.of(ratingResponse, ratingResponse, ratingResponse),
                currentPage,
                ratingPage.getTotalElements(),
                ratingPage.getTotalPages()
            ));

        PageResponse<RatingResponse> result = ratingServiceImpl.getRatings(currentPage, limit);

        assertNotNull(result);
        assertEquals(3, result.content().size());
        assertEquals(currentPage, result.currentPage());
        assertEquals(ratingPage.getTotalPages(), result.totalPages());
        assertEquals(ratingPage.getTotalElements(), result.totalElements());

        verify(ratingRepository).findAll(pageRequest);
        verify(pageResponseMapper).toPageResponse(eq(ratingPage), eq(currentPage), any());
    }

    @Test
    void getAverageRatingByPassengerId_shouldReturnAverageRatingResponse() {
        Long passengerId = UnitTestDataProvider.TEST_PASSENGER_ID;

        when(ratingRepository.findByRideInfo_PassengerIdAndRatedBy(
            eq(passengerId),
            any(PageRequest.class),
            eq(RatedBy.DRIVER)
        )).thenReturn(List.of(rating, rating, rating));

        AverageRatingResponse result = ratingServiceImpl.getAverageRatingByPassengerId(passengerId);

        assertEquals(averageRatingResponse, result);

        verify(ratingRepository).findByRideInfo_PassengerIdAndRatedBy(
            eq(passengerId),
            any(PageRequest.class),
            eq(RatedBy.DRIVER)
        );
    }

    @Test
    void getAverageRatingByPassengerId_shouldThrowNotFoundException() {
        Long passengerId = UnitTestDataProvider.TEST_PASSENGER_ID;

        when(ratingRepository.findByRideInfo_PassengerIdAndRatedBy(
            eq(passengerId),
            any(PageRequest.class),
            eq(RatedBy.DRIVER)
        )).thenReturn(List.of());

        RatingsNotFoundedByPassengerIdException exception = assertThrows(
            RatingsNotFoundedByPassengerIdException.class,
            () -> ratingServiceImpl.getAverageRatingByPassengerId(passengerId)
        );

        assertEquals(ExceptionMessageConstant.RATINGS_NOT_FOUND_BY_PASSENGER_ID_MESSAGE.formatted(passengerId),
            exception.getMessage());

        verify(ratingRepository).findByRideInfo_PassengerIdAndRatedBy(
            eq(passengerId),
            any(PageRequest.class),
            eq(RatedBy.DRIVER)
        );
    }

    @Test
    void getAverageRatingByDriverId_shouldReturnAverageRatingResponse() {
        Long driverId = UnitTestDataProvider.TEST_DRIVER_ID;

        when(ratingRepository.findByRideInfo_DriverIdAndRatedBy(
            eq(driverId),
            any(PageRequest.class),
            eq(RatedBy.PASSENGER)
        )).thenReturn(List.of(rating, rating, rating));

        AverageRatingResponse result = ratingServiceImpl.getAverageRatingByDriverId(driverId);

        assertEquals(averageRatingResponse, result);

        verify(ratingRepository).findByRideInfo_DriverIdAndRatedBy(
            eq(driverId),
            any(PageRequest.class),
            eq(RatedBy.PASSENGER)
        );
    }

    @Test
    void getAverageRatingByDriverId_shouldThrowNotFoundException() {
        Long driverId = UnitTestDataProvider.TEST_DRIVER_ID;

        when(ratingRepository.findByRideInfo_DriverIdAndRatedBy(
            eq(driverId),
            any(PageRequest.class),
            eq(RatedBy.PASSENGER)
        )).thenReturn(List.of());

        RatingsNotFoundedByDriverIdException exception = assertThrows(
            RatingsNotFoundedByDriverIdException.class,
            () -> ratingServiceImpl.getAverageRatingByDriverId(driverId)
        );

        assertEquals(ExceptionMessageConstant.RATINGS_NOT_FOUNDED_BY_DRIVER_ID_MESSAGE.formatted(driverId),
            exception.getMessage());

        verify(ratingRepository).findByRideInfo_DriverIdAndRatedBy(
            eq(driverId),
            any(PageRequest.class),
            eq(RatedBy.PASSENGER)
        );
    }

    @Test
    void createRating_shouldReturnRatingResponse() {
        Long requestRideId = UnitTestDataProvider.TEST_RIDE_ID;

        when(rideInfoService.getRideInfoByRideId(requestRideId)).thenReturn(
            UnitTestDataProvider.rideInfo());
        when(ratingMapper.toEntity(createRatingRequest)).thenReturn(rating);
        when(ratingRepository.save(rating)).thenReturn(rating);
        when(ratingMapper.toResponse(rating)).thenReturn(ratingResponse);

        RatingResponse result = ratingServiceImpl.createRating(createRatingRequest);

        assertNotNull(result);
        assertEquals(ratingResponse, result);

        verify(ratingValidator).validateCreateRating(requestRideId, RatedBy.DRIVER);
        verify(rideInfoService).getRideInfoByRideId(requestRideId);
        verify(ratingMapper).toEntity(createRatingRequest);
        verify(ratingRepository).save(rating);
        verify(ratingMapper).toResponse(rating);
    }

    @Test
    void updateRating_shouldReturnRatingResponse() {
        Long existingRatingId = rating.getId();

        when(ratingRepository.findById(existingRatingId)).thenReturn(Optional.of(rating));
        when(ratingRepository.save(rating)).thenReturn(rating);
        when(ratingMapper.toResponse(rating)).thenReturn(ratingResponse);

        RatingResponse result = ratingServiceImpl.updateRating(existingRatingId, updateRatingRequest);

        assertNotNull(result);
        assertEquals(ratingResponse, result);

        verify(ratingRepository).findById(existingRatingId);
        verify(ratingMapper).updateFromRequest(updateRatingRequest, rating);
        verify(ratingRepository).save(rating);
        verify(ratingMapper).toResponse(rating);
    }
}