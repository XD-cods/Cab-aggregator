package com.vlad.kuzhyr.driverservice.utility.broker.unittest;

import com.vlad.kuzhyr.driverservice.constant.UnitTestDataProvider;
import com.vlad.kuzhyr.driverservice.exception.DriverNotFoundException;
import com.vlad.kuzhyr.driverservice.persistence.entity.Driver;
import com.vlad.kuzhyr.driverservice.persistence.repository.DriverRepository;
import com.vlad.kuzhyr.driverservice.utility.broker.DriverProcessor;
import com.vlad.kuzhyr.driverservice.utility.constant.ExceptionMessageConstant;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DriverProcessorTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverProcessor driverProcessor;

    private Driver driver;

    @BeforeEach
    void setUp() {
        driver = UnitTestDataProvider.driver();
    }

    @ParameterizedTest
    @CsvSource({
        "true",
        "false"
    })
    void updateDriverByIdAndIsBusy_shouldUpdateDriverIsBusy(boolean isBusy) {
        Long existingDriverId = driver.getId();

        when(driverRepository.findById(existingDriverId)).thenReturn(Optional.of(driver));
        when(driverRepository.save(driver)).thenReturn(driver);

        driverProcessor.updateDriverByIdAndIsBusy(existingDriverId, isBusy);

        assertEquals(isBusy, driver.getIsBusy());

        verify(driverRepository).findById(existingDriverId);
        verify(driverRepository).save(driver);
    }

    @Test
    void updateDriverByIdAndIsBusy_shouldThrowDriverNotFoundException() {
        Long nonExistingDriverId = -1L;

        when(driverRepository.findById(nonExistingDriverId)).thenReturn(Optional.empty());

        DriverNotFoundException exception = assertThrows(DriverNotFoundException.class,
            () -> driverProcessor.updateDriverByIdAndIsBusy(nonExistingDriverId, true));

        assertEquals(ExceptionMessageConstant.DRIVER_NOT_FOUND_MESSAGE.formatted(nonExistingDriverId),
            exception.getMessage());

        verify(driverRepository).findById(nonExistingDriverId);
        verify(driverRepository, never()).save(any());
    }

}
