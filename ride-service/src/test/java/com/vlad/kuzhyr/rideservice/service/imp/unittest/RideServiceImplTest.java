package com.vlad.kuzhyr.rideservice.service.imp.unittest;

import com.vlad.kuzhyr.rideservice.constant.UnitTestDataProvider;
import com.vlad.kuzhyr.rideservice.exception.RideNotFoundException;
import com.vlad.kuzhyr.rideservice.persistence.entity.Ride;
import com.vlad.kuzhyr.rideservice.persistence.entity.RideStatus;
import com.vlad.kuzhyr.rideservice.persistence.repository.RideRepository;
import com.vlad.kuzhyr.rideservice.service.impl.AddressService;
import com.vlad.kuzhyr.rideservice.service.impl.RideServiceImpl;
import com.vlad.kuzhyr.rideservice.utility.broker.RideEventProducer;
import com.vlad.kuzhyr.rideservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.rideservice.utility.mapper.PageResponseMapper;
import com.vlad.kuzhyr.rideservice.utility.mapper.RideMapper;
import com.vlad.kuzhyr.rideservice.utility.validation.RideValidation;
import com.vlad.kuzhyr.rideservice.web.dto.external.RideInfoPayload;
import com.vlad.kuzhyr.rideservice.web.dto.request.RideRequest;
import com.vlad.kuzhyr.rideservice.web.dto.request.UpdateRideRequest;
import com.vlad.kuzhyr.rideservice.web.dto.request.UpdateRideStatusRequest;
import com.vlad.kuzhyr.rideservice.web.dto.response.PageResponse;
import com.vlad.kuzhyr.rideservice.web.dto.response.RideResponse;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

@ExtendWith(MockitoExtension.class)
public class RideServiceImplTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private RideMapper rideMapper;

    @Mock
    private PageResponseMapper pageResponseMapper;

    @Mock
    private RideValidation rideValidation;

    @Mock
    private RideEventProducer rideEventProducer;

    @Mock
    private AddressService addressService;

    @InjectMocks
    private RideServiceImpl rideServiceImpl;

    private Ride ride;
    private RideResponse rideResponse;

    @BeforeEach
    void setUp() {
        ride = UnitTestDataProvider.ride();
        rideResponse = UnitTestDataProvider.rideResponse();
    }

    @Test
    void getRideById_shouldReturnRideResponse() {
        Long existingRideId = ride.getId();

        when(rideRepository.findById(existingRideId)).thenReturn(Optional.of(ride));
        when(rideMapper.toResponse(ride)).thenReturn(rideResponse);

        RideResponse result = rideServiceImpl.getRideById(existingRideId);

        assertNotNull(result);
        assertEquals(rideResponse, result);

        verify(rideRepository).findById(existingRideId);
        verify(rideMapper).toResponse(ride);
    }

    @Test
    void getRideById_shouldThrowNotFoundException() {
        Long nonExistingRideId = 2L;

        when(rideRepository.findById(nonExistingRideId)).thenReturn(Optional.empty());

        RideNotFoundException exception = assertThrows(
            RideNotFoundException.class,
            () -> rideServiceImpl.getRideById(nonExistingRideId)
        );

        assertEquals(
            ExceptionMessageConstant.RIDE_NOT_FOUND_MESSAGE.formatted(nonExistingRideId),
            exception.getMessage()
        );

        verify(rideRepository).findById(nonExistingRideId);
        verifyNoInteractions(rideMapper);
    }

    @Test
    void getAllRidesByDriverId_shouldReturnPageResponse() {
        Long driverId = UnitTestDataProvider.TEST_DRIVER_ID;
        int currentPage = 0;
        int limit = 10;
        PageRequest pageRequest = PageRequest.of(currentPage, limit);

        List<Ride> rides = List.of(ride);
        Page<Ride> ridePage = new PageImpl<>(rides, pageRequest, rides.size());

        when(rideRepository.findByDriverId(driverId, pageRequest)).thenReturn(ridePage);
        when(pageResponseMapper.toPageResponse(eq(ridePage), eq(currentPage), any()))
            .thenReturn(new PageResponse<>(List.of(rideResponse), currentPage, ridePage.getTotalElements(),
                ridePage.getTotalPages()));

        PageResponse<RideResponse> result = rideServiceImpl.getAllRidesByDriverId(driverId, currentPage, limit);

        assertNotNull(result);
        assertEquals(rides.size(), result.content().size());
        assertEquals(currentPage, result.currentPage());
        assertEquals(ridePage.getTotalPages(), result.totalPages());
        assertEquals(ridePage.getTotalElements(), result.totalElements());

        verify(rideRepository).findByDriverId(driverId, pageRequest);
        verify(pageResponseMapper).toPageResponse(eq(ridePage), eq(currentPage), any());
    }

    @Test
    void getAllRidesByPassengerId_shouldReturnPageResponse() {
        Long passengerId = UnitTestDataProvider.TEST_PASSENGER_ID;
        int currentPage = 0;
        int limit = 10;
        PageRequest pageRequest = PageRequest.of(currentPage, limit);

        List<Ride> rides = List.of(ride);
        Page<Ride> ridePage = new PageImpl<>(rides, pageRequest, rides.size());

        when(rideRepository.findByPassengerId(passengerId, pageRequest)).thenReturn(ridePage);
        when(pageResponseMapper.toPageResponse(eq(ridePage), eq(currentPage), any()))
            .thenReturn(new PageResponse<>(List.of(rideResponse), currentPage, ridePage.getTotalElements(),
                ridePage.getTotalPages()));

        PageResponse<RideResponse> result = rideServiceImpl.getAllRidesByPassengerId(passengerId, currentPage, limit);

        assertNotNull(result);
        assertEquals(rides.size(), result.content().size());
        assertEquals(currentPage, result.currentPage());
        assertEquals(ridePage.getTotalPages(), result.totalPages());
        assertEquals(ridePage.getTotalElements(), result.totalElements());

        verify(rideRepository).findByPassengerId(passengerId, pageRequest);
        verify(pageResponseMapper).toPageResponse(eq(ridePage), eq(currentPage), any());
    }

    @Test
    void getAllRides_shouldReturnPageResponse() {
        int currentPage = 0;
        int limit = 10;
        PageRequest pageRequest = PageRequest.of(currentPage, limit);

        List<Ride> rides = List.of(ride);
        Page<Ride> ridePage = new PageImpl<>(rides, pageRequest, rides.size());

        when(rideRepository.findAll(pageRequest)).thenReturn(ridePage);
        when(pageResponseMapper.toPageResponse(eq(ridePage), eq(currentPage), any()))
            .thenReturn(new PageResponse<>(List.of(rideResponse), currentPage, ridePage.getTotalElements(),
                ridePage.getTotalPages()));

        PageResponse<RideResponse> result = rideServiceImpl.getAllRides(currentPage, limit);

        assertNotNull(result);
        assertEquals(rides.size(), result.content().size());
        assertEquals(currentPage, result.currentPage());
        assertEquals(ridePage.getTotalPages(), result.totalPages());
        assertEquals(ridePage.getTotalElements(), result.totalElements());

        verify(rideRepository).findAll(pageRequest);
        verify(pageResponseMapper).toPageResponse(eq(ridePage), eq(currentPage), any());
    }

    @Test
    void updateRide_shouldReturnRideResponse() {
        Long existingRideId = ride.getId();
        UpdateRideRequest updateRequest = UnitTestDataProvider.updateRideRequest();

        when(rideRepository.findById(existingRideId)).thenReturn(Optional.of(ride));
        when(rideRepository.save(ride)).thenReturn(ride);
        when(rideMapper.toResponse(ride)).thenReturn(rideResponse);

        RideResponse result = rideServiceImpl.updateRide(existingRideId, updateRequest);

        assertNotNull(result);
        assertEquals(rideResponse, result);

        verify(rideRepository).findById(existingRideId);
        verify(rideRepository).save(ride);
        verify(rideMapper).toResponse(ride);
    }

    @Test
    void updateRideStatus_whenStatusIsPassengerPickedUp_shouldSetPickupTime() {
        Long rideId = UnitTestDataProvider.TEST_ID;
        Ride ride = UnitTestDataProvider.ride();
        ride.setRideStatus(RideStatus.ON_THE_WAY_TO_PASSENGER);
        UpdateRideStatusRequest updateRequest = new UpdateRideStatusRequest(RideStatus.PASSENGER_PICKED_UP);

        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(rideRepository.save(ride)).thenReturn(ride);
        when(rideMapper.toResponse(ride)).thenReturn(UnitTestDataProvider.rideResponse());

        RideResponse result = rideServiceImpl.updateRideStatus(rideId, updateRequest);

        assertNotNull(result);
        assertNotNull(ride.getPickupTime());

        verifyNoInteractions(rideEventProducer);
        verifyNoInteractions(rideEventProducer);
        verify(rideRepository).findById(rideId);
        verify(rideRepository).save(any(Ride.class));
        verify(rideMapper).toResponse(any(Ride.class));
    }

    @Test
    void updateRideStatus_whenStatusIsCompleted_shouldSetCompleteTime() {
        Long rideId = UnitTestDataProvider.TEST_ID;
        Ride ride = UnitTestDataProvider.ride();
        ride.setRideStatus(RideStatus.ON_THE_WAY);
        UpdateRideStatusRequest updateRequest = new UpdateRideStatusRequest(RideStatus.COMPLETED);

        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(rideRepository.save(ride)).thenReturn(ride);
        when(rideMapper.toResponse(ride)).thenReturn(UnitTestDataProvider.rideResponse());

        RideResponse result = rideServiceImpl.updateRideStatus(rideId, updateRequest);

        assertNotNull(result);
        assertNotNull(ride.getCompleteTime());

        verify(rideEventProducer).sendDriverBusyMessage(ride.getDriverId(), false);
        verify(rideEventProducer).sendPassengerBusyTopic(ride.getPassengerId(), false);
        verify(rideRepository).findById(rideId);
        verify(rideRepository).save(any(Ride.class));
        verify(rideMapper).toResponse(any(Ride.class));
    }

    @Test
    void updateRideStatus_whenStatusIsRate_shouldSendRideCompletedMessage() {
        Long rideId = UnitTestDataProvider.TEST_ID;
        Ride ride = UnitTestDataProvider.ride();
        ride.setRideStatus(RideStatus.PAID);
        UpdateRideStatusRequest updateRequest = new UpdateRideStatusRequest(RideStatus.RATE);

        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(rideRepository.save(ride)).thenReturn(ride);
        when(rideMapper.toResponse(ride)).thenReturn(UnitTestDataProvider.rideResponse());

        RideResponse result = rideServiceImpl.updateRideStatus(rideId, updateRequest);

        assertNotNull(result);

        verify(rideEventProducer).sendRideCompletedMessage(any(RideInfoPayload.class));
        verify(rideRepository).findById(rideId);
        verify(rideRepository).save(any(Ride.class));
        verify(rideMapper).toResponse(any(Ride.class));
    }

    @Test
    void updateRideStatus_whenStatusIsCancelled_shouldSetDriverAndPassengerNotBusy() {
        Long rideId = UnitTestDataProvider.TEST_ID;
        Ride ride = UnitTestDataProvider.ride();
        ride.setRideStatus(RideStatus.ACCEPTED);
        UpdateRideStatusRequest updateRequest = new UpdateRideStatusRequest(RideStatus.CANCELLED);

        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(rideRepository.save(ride)).thenReturn(ride);
        when(rideMapper.toResponse(ride)).thenReturn(UnitTestDataProvider.rideResponse());

        RideResponse result = rideServiceImpl.updateRideStatus(rideId, updateRequest);

        assertNotNull(result);

        verify(rideEventProducer).sendDriverBusyMessage(ride.getDriverId(), false);
        verify(rideEventProducer).sendPassengerBusyTopic(ride.getPassengerId(), false);
        verify(rideRepository).findById(rideId);
        verify(rideRepository).save(any(Ride.class));
        verify(rideMapper).toResponse(any(Ride.class));
    }

    @Test
    void updateRideStatus_whenStatusIsDefault_shouldDoNothing() {
        Long rideId = UnitTestDataProvider.TEST_ID;
        Ride ride = UnitTestDataProvider.ride();
        ride.setRideStatus(RideStatus.CREATED);
        UpdateRideStatusRequest updateRequest = new UpdateRideStatusRequest(RideStatus.WAITING_FOR_DRIVER);

        when(rideRepository.findById(rideId)).thenReturn(Optional.of(ride));
        when(rideRepository.save(ride)).thenReturn(ride);
        when(rideMapper.toResponse(ride)).thenReturn(UnitTestDataProvider.rideResponse());

        RideResponse result = rideServiceImpl.updateRideStatus(rideId, updateRequest);

        assertNotNull(result);
        assertNull(ride.getPickupTime());
        assertNull(ride.getCompleteTime());

        verifyNoInteractions(rideEventProducer);
        verify(rideRepository).findById(rideId);
        verify(rideRepository).save(any(Ride.class));
        verify(rideMapper).toResponse(any(Ride.class));
    }

    @Test
    void createRide_shouldReturnRideResponse() {
        RideRequest rideRequest = UnitTestDataProvider.rideRequest();

        when(rideMapper.toEntity(rideRequest)).thenReturn(ride);
        when(rideRepository.save(ride)).thenReturn(ride);
        when(rideMapper.toResponse(ride)).thenReturn(rideResponse);

        RideResponse result = rideServiceImpl.createRide(rideRequest);

        assertNotNull(result);
        assertEquals(rideResponse, result);

        verify(rideMapper).toEntity(rideRequest);
        verify(rideRepository).save(ride);
        verify(rideMapper).toResponse(ride);
    }
}