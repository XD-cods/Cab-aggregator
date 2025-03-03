package com.vlad.kuzhyr.ratingservice.service.impl.unittest;

import com.vlad.kuzhyr.ratingservice.constant.UnitTestDataProvider;
import com.vlad.kuzhyr.ratingservice.exception.RideInfoPayloadNotFoundException;
import com.vlad.kuzhyr.ratingservice.persistence.entity.RideInfo;
import com.vlad.kuzhyr.ratingservice.persistence.repository.RideInfoRepository;
import com.vlad.kuzhyr.ratingservice.service.impl.RideInfoServiceImpl;
import com.vlad.kuzhyr.ratingservice.utility.constant.ExceptionMessageConstant;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RideInfoServiceImplTest {

    @Mock
    private RideInfoRepository rideInfoRepository;

    @InjectMocks
    private RideInfoServiceImpl rideInfoServiceImpl;

    private RideInfo rideInfo;

    @BeforeEach
    void setUp() {
        rideInfo = UnitTestDataProvider.rideInfo();
    }

    @Test
    void getRideInfoByRideId_shouldReturnRideInfo() {
        Long rideId = UnitTestDataProvider.TEST_RIDE_ID;

        when(rideInfoRepository.findByRideId(rideId)).thenReturn(Optional.of(rideInfo));

        RideInfo result = rideInfoServiceImpl.getRideInfoByRideId(rideId);

        assertEquals(rideInfo, result);

        verify(rideInfoRepository).findByRideId(rideId);
    }

    @Test
    void getRideInfoByRideId_shouldThrowNotFoundException() {
        Long nonExistingRideId = 2L;

        when(rideInfoRepository.findByRideId(nonExistingRideId)).thenReturn(Optional.empty());

        RideInfoPayloadNotFoundException exception = assertThrows(
            RideInfoPayloadNotFoundException.class,
            () -> rideInfoServiceImpl.getRideInfoByRideId(nonExistingRideId)
        );

        assertEquals(
            ExceptionMessageConstant.RIDE_INFO_NOT_FOUND_MESSAGE.formatted(nonExistingRideId),
            exception.getMessage()
        );

        verify(rideInfoRepository).findByRideId(nonExistingRideId);
    }

    @Test
    void saveRideInfo_shouldSaveRideInfo() {
        when(rideInfoRepository.save(rideInfo)).thenReturn(rideInfo);

        rideInfoServiceImpl.saveRideInfo(rideInfo);

        verify(rideInfoRepository).save(rideInfo);
    }
}