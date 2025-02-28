package com.vlad.kuzhyr.driverservice.service.impl.unittest;

import com.vlad.kuzhyr.driverservice.constant.UnitTestDataProvider;
import com.vlad.kuzhyr.driverservice.exception.DriverNotFoundException;
import com.vlad.kuzhyr.driverservice.persistence.entity.Car;
import com.vlad.kuzhyr.driverservice.persistence.entity.Driver;
import com.vlad.kuzhyr.driverservice.persistence.repository.CarRepository;
import com.vlad.kuzhyr.driverservice.persistence.repository.DriverRepository;
import com.vlad.kuzhyr.driverservice.service.impl.DriverServiceImpl;
import com.vlad.kuzhyr.driverservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.driverservice.utility.mapper.DriverMapper;
import com.vlad.kuzhyr.driverservice.utility.mapper.PageResponseMapper;
import com.vlad.kuzhyr.driverservice.utility.validator.DriverValidator;
import com.vlad.kuzhyr.driverservice.web.dto.request.DriverRequest;
import com.vlad.kuzhyr.driverservice.web.dto.request.DriverUpdateCarsRequest;
import com.vlad.kuzhyr.driverservice.web.dto.response.DriverResponse;
import com.vlad.kuzhyr.driverservice.web.dto.response.PageResponse;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class DriverServiceImplTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private DriverValidator driverValidator;

    @Mock
    private PageResponseMapper pageResponseMapper;

    @Mock
    private DriverMapper driverMapper;

    @InjectMocks
    private DriverServiceImpl driverServiceImpl;

    private Driver driver;
    private DriverResponse driverResponse;
    private Car car;

    @BeforeEach
    void setUp() {
        driver = UnitTestDataProvider.driver();
        driverResponse = UnitTestDataProvider.driverResponse();
        car = UnitTestDataProvider.car();
    }

    @Test
    void getDriverById_shouldReturnDriverResponse() {
        Long existingDriverId = driver.getId();

        when(driverRepository.findDriverByIdAndIsEnabledTrue(existingDriverId))
            .thenReturn(Optional.of(driver));
        when(driverMapper.toResponse(driver)).thenReturn(driverResponse);

        DriverResponse result = driverServiceImpl.getDriverById(existingDriverId);

        assertNotNull(result);
        assertEquals(driverResponse, result);

        verify(driverRepository).findDriverByIdAndIsEnabledTrue(existingDriverId);
        verify(driverMapper).toResponse(driver);
    }

    @Test
    void getDriverById_shouldThrowNotFoundException() {
        Long nonExistingDriverId = 2L;

        when(driverRepository.findDriverByIdAndIsEnabledTrue(nonExistingDriverId))
            .thenReturn(Optional.empty());

        DriverNotFoundException exception = assertThrows(
            DriverNotFoundException.class,
            () -> driverServiceImpl.getDriverById(nonExistingDriverId)
        );

        assertEquals(
            ExceptionMessageConstant.DRIVER_NOT_FOUND_MESSAGE.formatted(nonExistingDriverId),
            exception.getMessage()
        );

        verify(driverRepository).findDriverByIdAndIsEnabledTrue(nonExistingDriverId);
        verifyNoInteractions(driverMapper);
    }

    @ParameterizedTest
    @CsvSource({
        "0, 10",
        "1, 20",
        "2, 5"
    })
    void getAllDriver_shouldReturnPageResponse(int currentPage, int limit) {
        PageRequest pageRequest = PageRequest.of(currentPage, limit);

        List<Driver> drivers = List.of(driver);
        Page<Driver> driverPage = new PageImpl<>(drivers, pageRequest, drivers.size());

        when(driverRepository.findAll(pageRequest)).thenReturn(driverPage);
        when(pageResponseMapper.toPageResponse(eq(driverPage), eq(currentPage), any()))
            .thenReturn(new PageResponse<>(List.of(driverResponse), currentPage, driverPage.getTotalElements(),
                driverPage.getTotalPages()));

        PageResponse<DriverResponse> result = driverServiceImpl.getAllDriver(currentPage, limit);

        assertNotNull(result);
        assertEquals(drivers.size(), result.content().size());
        assertEquals(currentPage, result.currentPage());
        assertEquals(driverPage.getTotalPages(), result.totalPages());
        assertEquals(driverPage.getTotalElements(), result.totalElements());

        verify(driverRepository).findAll(pageRequest);
        verify(pageResponseMapper).toPageResponse(eq(driverPage), eq(currentPage), any());
    }

    @Test
    void createDriver_shouldReturnDriverResponse() {
        DriverRequest driverRequest = UnitTestDataProvider.driverCreateRequest();
        DriverResponse driverCreateResponse = UnitTestDataProvider.driverCreateResponse();
        String driverRequestEmail = UnitTestDataProvider.TEST_DRIVER_EMAIL;
        String driverRequestPhone = UnitTestDataProvider.TEST_DRIVER_PHONE;

        when(driverMapper.toEntity(driverRequest)).thenReturn(driver);
        when(carRepository.findAllById(driverRequest.carIds())).thenReturn(List.of(car));
        when(driverRepository.save(driver)).thenReturn(driver);
        when(driverMapper.toResponse(driver)).thenReturn(driverCreateResponse);

        DriverResponse result = driverServiceImpl.createDriver(driverRequest);

        assertNotNull(result);
        assertEquals(driverCreateResponse, result);

        verify(driverValidator).validateDriver(driverRequestEmail, driverRequestPhone);
        verify(driverMapper).toEntity(driverRequest);
        verify(carRepository).findAllById(driverRequest.carIds());
        verify(driverRepository).save(driver);
        verify(driverMapper).toResponse(driver);
    }

    @Test
    void updateDriver_shouldReturnDriverResponse() {
        Long existingDriverId = driver.getId();
        DriverRequest updateRequest = UnitTestDataProvider.driverUpdateRequest();
        DriverResponse updatedResponse = UnitTestDataProvider.driverUpdateResponse();

        when(driverRepository.findDriverByIdAndIsEnabledTrue(existingDriverId))
            .thenReturn(Optional.of(driver));
        when(carRepository.findAllById(updateRequest.carIds())).thenReturn(List.of(car));
        when(driverRepository.save(driver)).thenReturn(driver);
        when(driverMapper.toResponse(driver)).thenReturn(updatedResponse);

        DriverResponse result = driverServiceImpl.updateDriver(existingDriverId, updateRequest);

        assertNotNull(result);
        assertEquals(updatedResponse, result);

        verify(driverValidator).validateDriver(updateRequest.email(), updateRequest.phone());
        verify(driverRepository).findDriverByIdAndIsEnabledTrue(existingDriverId);
        verify(driverMapper).updateFromRequest(updateRequest, driver);
        verify(carRepository).findAllById(updateRequest.carIds());
        verify(driverRepository).save(driver);
        verify(driverMapper).toResponse(driver);
    }

    @Test
    void updateDriverCarsById_shouldReturnDriverResponse() {
        Long existingDriverId = driver.getId();
        DriverUpdateCarsRequest updateCarsRequest = UnitTestDataProvider.driverUpdateCarsRequest();
        DriverResponse updatedResponse = UnitTestDataProvider.driverUpdateResponse();

        when(driverRepository.findDriverByIdAndIsEnabledTrue(existingDriverId))
            .thenReturn(Optional.of(driver));
        when(carRepository.findAllById(updateCarsRequest.carIds())).thenReturn(List.of(car));
        when(driverRepository.save(driver)).thenReturn(driver);
        when(driverMapper.toResponse(driver)).thenReturn(updatedResponse);

        DriverResponse result = driverServiceImpl.updateDriverCarsById(existingDriverId, updateCarsRequest);

        assertNotNull(result);
        assertEquals(updatedResponse, result);

        verify(driverRepository).findDriverByIdAndIsEnabledTrue(existingDriverId);
        verify(carRepository).findAllById(updateCarsRequest.carIds());
        verify(driverRepository).save(driver);
        verify(driverMapper).toResponse(driver);
    }

    @Test
    void deleteDriverById_shouldReturnTrue() {
        Long existingDriverId = driver.getId();

        when(driverRepository.findDriverByIdAndIsEnabledTrue(existingDriverId))
            .thenReturn(Optional.of(driver));

        Boolean result = driverServiceImpl.deleteDriverById(existingDriverId);

        assertTrue(result);
        assertFalse(driver.getIsEnabled());

        verify(driverRepository).findDriverByIdAndIsEnabledTrue(existingDriverId);
        verify(driverRepository).save(driver);
    }

    @Test
    void deleteDriverById_shouldThrowNotFoundException() {
        Long nonExistingDriverId = 2L;

        when(driverRepository.findDriverByIdAndIsEnabledTrue(nonExistingDriverId))
            .thenReturn(Optional.empty());

        DriverNotFoundException exception = assertThrows(
            DriverNotFoundException.class,
            () -> driverServiceImpl.deleteDriverById(nonExistingDriverId)
        );

        assertEquals(
            ExceptionMessageConstant.DRIVER_NOT_FOUND_MESSAGE.formatted(nonExistingDriverId),
            exception.getMessage()
        );

        verify(driverRepository).findDriverByIdAndIsEnabledTrue(nonExistingDriverId);
        verifyNoInteractions(driverMapper);
        verify(driverRepository, never()).save(driver);
    }
}