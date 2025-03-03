package com.vlad.kuzhyr.rideservice.utility.validator.unittest;

import com.vlad.kuzhyr.rideservice.constant.UnitTestDataProvider;
import com.vlad.kuzhyr.rideservice.exception.DriverHasNotCarException;
import com.vlad.kuzhyr.rideservice.exception.DriverIsBusyException;
import com.vlad.kuzhyr.rideservice.exception.NotValidStatusTransitionException;
import com.vlad.kuzhyr.rideservice.exception.PassengerIsBusyException;
import com.vlad.kuzhyr.rideservice.exception.RideCanNotUpdatableException;
import com.vlad.kuzhyr.rideservice.exception.RidesNotFoundByDriverIdException;
import com.vlad.kuzhyr.rideservice.exception.RidesNotFoundByPassengerIdException;
import com.vlad.kuzhyr.rideservice.persistence.entity.Ride;
import com.vlad.kuzhyr.rideservice.persistence.entity.RideStatus;
import com.vlad.kuzhyr.rideservice.persistence.repository.RideRepository;
import com.vlad.kuzhyr.rideservice.utility.client.DriverFeignClient;
import com.vlad.kuzhyr.rideservice.utility.client.PassengerFeignClient;
import com.vlad.kuzhyr.rideservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.rideservice.utility.validation.RideValidation;
import com.vlad.kuzhyr.rideservice.web.dto.external.DriverResponse;
import com.vlad.kuzhyr.rideservice.web.dto.external.PassengerResponse;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class RideValidationTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private PassengerFeignClient passengerFeignClient;

    @Mock
    private DriverFeignClient driverFeignClient;

    @InjectMocks
    private RideValidation rideValidation;

    private Ride ride;
    private DriverResponse driverResponse;
    private PassengerResponse passengerResponse;

    @BeforeEach
    void setUp() {
        ride = UnitTestDataProvider.ride();
        driverResponse = UnitTestDataProvider.driverResponse();
        passengerResponse = UnitTestDataProvider.passengerResponse();
    }

    @Test
    void validateRidesExistByDriverId_shouldThrowExceptionWhenNoRidesFound() {
        Long driverId = UnitTestDataProvider.TEST_DRIVER_ID;

        when(rideRepository.existsRidesByDriverId(driverId)).thenReturn(false);

        RidesNotFoundByDriverIdException exception = assertThrows(RidesNotFoundByDriverIdException.class,
            () -> rideValidation.validateRidesExistByDriverId(driverId));

        assertEquals(ExceptionMessageConstant.RIDES_NOT_FOUND_BY_DRIVER_ID_MESSAGE.formatted(driverId),
            exception.getMessage());

        verify(rideRepository).existsRidesByDriverId(driverId);
        verifyNoInteractions(passengerFeignClient);
        verifyNoInteractions(driverFeignClient);
    }

    @Test
    void validateRidesExistByDriverId_shouldNotThrowExceptionWhenRidesFound() {
        Long driverId = UnitTestDataProvider.TEST_DRIVER_ID;

        when(rideRepository.existsRidesByDriverId(driverId)).thenReturn(true);

        assertDoesNotThrow(() -> rideValidation.validateRidesExistByDriverId(driverId));

        verify(rideRepository).existsRidesByDriverId(driverId);
        verifyNoInteractions(passengerFeignClient);
        verifyNoInteractions(driverFeignClient);
    }

    @Test
    void validateRidesExistByPassengerId_shouldThrowExceptionWhenNoRidesFound() {
        Long passengerId = UnitTestDataProvider.TEST_PASSENGER_ID;

        when(rideRepository.existsRidesByPassengerId(passengerId)).thenReturn(false);

        RidesNotFoundByPassengerIdException exception = assertThrows(RidesNotFoundByPassengerIdException.class,
            () -> rideValidation.validateRidesExistByPassengerId(passengerId));

        assertEquals(ExceptionMessageConstant.RIDES_NOT_FOUND_BY_PASSENGER_ID_MESSAGE.formatted(passengerId),
            exception.getMessage());

        verify(rideRepository).existsRidesByPassengerId(passengerId);
        verifyNoInteractions(passengerFeignClient);
        verifyNoInteractions(driverFeignClient);
    }

    @Test
    void validateRidesExistByPassengerId_shouldNotThrowExceptionWhenRidesFound() {
        Long passengerId = UnitTestDataProvider.TEST_PASSENGER_ID;

        when(rideRepository.existsRidesByPassengerId(passengerId)).thenReturn(true);

        assertDoesNotThrow(() -> rideValidation.validateRidesExistByPassengerId(passengerId));

        verify(rideRepository).existsRidesByPassengerId(passengerId);
        verifyNoInteractions(passengerFeignClient);
        verifyNoInteractions(driverFeignClient);
    }

    @Test
    void validateRideIsUpdatable_shouldThrowExceptionWhenRideNotUpdatable() {
        ride.setRideStatus(RideStatus.COMPLETED);

        assertThrows(RideCanNotUpdatableException.class, () -> rideValidation.validateRideIsUpdatable(ride));

        verifyNoInteractions(rideRepository);
        verifyNoInteractions(passengerFeignClient);
        verifyNoInteractions(driverFeignClient);
    }

    @Test
    void validateRideIsUpdatable_shouldNotThrowExceptionWhenRideUpdatable() {
        ride.setRideStatus(RideStatus.CREATED);

        assertDoesNotThrow(() -> rideValidation.validateRideIsUpdatable(ride));

        verifyNoInteractions(rideRepository);
        verifyNoInteractions(passengerFeignClient);
        verifyNoInteractions(driverFeignClient);
    }

    @Test
    void checkDriverAndPassengerAvailability_shouldNotThrowException() {
        Long driverId = UnitTestDataProvider.TEST_DRIVER_ID;
        Long passengerId = UnitTestDataProvider.TEST_PASSENGER_ID;

        when(driverFeignClient.getDriverById(driverId)).thenReturn(
            ResponseEntity.ok(UnitTestDataProvider.driverResponse()));
        when(passengerFeignClient.getPassengerById(passengerId)).thenReturn(ResponseEntity.ok(passengerResponse));

        assertDoesNotThrow(() -> rideValidation.checkDriverAndPassengerAvailability(driverId, passengerId));

        verify(driverFeignClient).getDriverById(driverId);
        verify(passengerFeignClient).getPassengerById(passengerId);
    }

    @Test
    void checkDriverAndPassengerAvailability_shouldThrowExceptionWhenDriverBusy() {
        Long driverId = UnitTestDataProvider.TEST_DRIVER_ID;
        Long passengerId = UnitTestDataProvider.TEST_PASSENGER_ID;

        when(driverFeignClient.getDriverById(driverId)).thenReturn(
            ResponseEntity.ok(UnitTestDataProvider.busyDriverResponse()));
        when(passengerFeignClient.getPassengerById(passengerId)).thenReturn(ResponseEntity.ok(passengerResponse));

        DriverIsBusyException exception = assertThrows(DriverIsBusyException.class,
            () -> rideValidation.checkDriverAndPassengerAvailability(driverId, passengerId));

        assertEquals(ExceptionMessageConstant.DRIVER_BUSY_MESSAGE.formatted(driverId), exception.getMessage());

        verify(driverFeignClient).getDriverById(driverId);
        verify(passengerFeignClient).getPassengerById(passengerId);
    }

    @Test
    void checkDriverAndPassengerAvailability_shouldThrowExceptionWhenPassengerBusy() {
        Long driverId = UnitTestDataProvider.TEST_DRIVER_ID;
        Long passengerId = UnitTestDataProvider.TEST_PASSENGER_ID;

        when(driverFeignClient.getDriverById(driverId)).thenReturn(ResponseEntity.ok(driverResponse));
        when(passengerFeignClient.getPassengerById(passengerId)).thenReturn(
            ResponseEntity.ok(UnitTestDataProvider.busyPassengerResponse()));

        PassengerIsBusyException exception = assertThrows(PassengerIsBusyException.class,
            () -> rideValidation.checkDriverAndPassengerAvailability(driverId, passengerId));

        assertEquals(ExceptionMessageConstant.PASSENGER_BUSY_MESSAGE.formatted(passengerId), exception.getMessage());

        verify(driverFeignClient).getDriverById(driverId);
        verify(passengerFeignClient).getPassengerById(passengerId);
    }

    @Test
    void checkDriverAndPassengerAvailability_shouldThrowExceptionWhenDriverHasNoCar() {
        Long driverId = UnitTestDataProvider.TEST_DRIVER_ID;
        Long passengerId = UnitTestDataProvider.TEST_PASSENGER_ID;

        when(driverFeignClient.getDriverById(driverId)).thenReturn(
            ResponseEntity.ok(UnitTestDataProvider.driverWithoutCarResponse()));
        when(passengerFeignClient.getPassengerById(passengerId)).thenReturn(ResponseEntity.ok(passengerResponse));

        DriverHasNotCarException exception = assertThrows(DriverHasNotCarException.class,
            () -> rideValidation.checkDriverAndPassengerAvailability(driverId, passengerId));

        assertEquals(ExceptionMessageConstant.DRIVER_HAS_NO_CAR.formatted(driverId), exception.getMessage());

        verify(driverFeignClient).getDriverById(driverId);
        verify(passengerFeignClient).getPassengerById(passengerId);
    }

    @Test
    void validateStatusTransition_shouldThrowExceptionWhenInvalidTransition() {
        NotValidStatusTransitionException exception = assertThrows(NotValidStatusTransitionException.class, () ->
            rideValidation.validateStatusTransition(RideStatus.COMPLETED, RideStatus.CREATED));

        assertEquals(exception.getMessage(),
            ExceptionMessageConstant.NOT_VALID_STATUS_TRANSITION.formatted(RideStatus.COMPLETED,
                RideStatus.CREATED));

        verifyNoInteractions(rideRepository);
        verifyNoInteractions(passengerFeignClient);
        verifyNoInteractions(driverFeignClient);
    }

    @Test
    void validateStatusTransition_shouldNotThrowExceptionWhenValidTransition() {
        assertDoesNotThrow(() ->
            rideValidation.validateStatusTransition(RideStatus.CREATED, RideStatus.WAITING_FOR_DRIVER));

        verifyNoInteractions(rideRepository);
        verifyNoInteractions(passengerFeignClient);
        verifyNoInteractions(driverFeignClient);
    }
}