package com.vlad.kuzhyr.passengerservice.utility.broker.unittest;

import com.vlad.kuzhyr.passengerservice.constant.UnitTestDataProvider;
import com.vlad.kuzhyr.passengerservice.exception.PassengerNotFoundException;
import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import com.vlad.kuzhyr.passengerservice.persistence.repository.PassengerRepository;
import com.vlad.kuzhyr.passengerservice.utility.broker.PassengerProcessor;
import com.vlad.kuzhyr.passengerservice.utility.constant.ExceptionMessageConstant;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PassengerProcessorTest {

    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private PassengerProcessor passengerProcessor;

    private Passenger passenger;

    @BeforeEach
    void setUp() {
        passenger = UnitTestDataProvider.passenger();
    }

    @ParameterizedTest
    @CsvSource({
        "true",
        "false"
    })
    void updatePassengerByIdAndIsBusy_shouldUpdatePassengerIsBusy(boolean isBusy) {
        Long existingPassengerId = passenger.getId();

        when(passengerRepository.findById(existingPassengerId)).thenReturn(Optional.of(passenger));
        when(passengerRepository.save(passenger)).thenReturn(passenger);

        passengerProcessor.updatePassengerByIdAndIsBusy(existingPassengerId, isBusy);

        assertEquals(isBusy, passenger.getIsBusy());

        verify(passengerRepository).findById(existingPassengerId);
        verify(passengerRepository).save(any());
    }

    @Test
    void updatePassengerByIdAndIsBusy_shouldThrowNotFoundPassengerException() {
        Long notExistingPassengerId = 0L;

        when(passengerRepository.findById(notExistingPassengerId)).thenReturn(Optional.empty());

        PassengerNotFoundException exception = assertThrows(PassengerNotFoundException.class,
            () -> passengerProcessor.updatePassengerByIdAndIsBusy(notExistingPassengerId, anyBoolean()));

        assertEquals(ExceptionMessageConstant.PASSENGER_NOT_FOUND_MESSAGE.formatted(notExistingPassengerId),
            exception.getMessage());

        verify(passengerRepository).findById(notExistingPassengerId);
        verify(passengerRepository, never()).save(passenger);
    }

}
