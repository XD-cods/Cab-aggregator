package com.vlad.kuzhyr.driverservice.service.impl.unittest;

import com.vlad.kuzhyr.driverservice.constant.UnitTestDataProvider;
import com.vlad.kuzhyr.driverservice.exception.CarNotFoundException;
import com.vlad.kuzhyr.driverservice.persistence.entity.Car;
import com.vlad.kuzhyr.driverservice.persistence.repository.CarRepository;
import com.vlad.kuzhyr.driverservice.service.impl.CarServiceImpl;
import com.vlad.kuzhyr.driverservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.driverservice.utility.mapper.CarMapper;
import com.vlad.kuzhyr.driverservice.utility.mapper.PageResponseMapper;
import com.vlad.kuzhyr.driverservice.utility.validator.CarValidator;
import com.vlad.kuzhyr.driverservice.web.dto.request.CarRequest;
import com.vlad.kuzhyr.driverservice.web.dto.response.CarResponse;
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
public class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CarValidator carValidator;

    @Mock
    private PageResponseMapper pageResponseMapper;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CarServiceImpl carServiceImpl;

    private Car car;
    private CarResponse carResponse;

    @BeforeEach
    void setUp() {
        car = UnitTestDataProvider.car();
        carResponse = UnitTestDataProvider.carResponse();
    }

    @Test
    void getCarById_shouldReturnCarResponse() {
        Long existingCarId = car.getId();

        when(carRepository.findCarByIdAndIsEnabledTrue(existingCarId))
            .thenReturn(Optional.of(car));
        when(carMapper.toResponse(car)).thenReturn(carResponse);

        CarResponse result = carServiceImpl.getCarById(existingCarId);

        assertNotNull(result);
        assertEquals(carResponse, result);

        verify(carRepository).findCarByIdAndIsEnabledTrue(existingCarId);
        verify(carMapper).toResponse(car);
    }

    @Test
    void getCarById_shouldThrowNotFoundException() {
        Long nonExistingCarId = 2L;

        when(carRepository.findCarByIdAndIsEnabledTrue(nonExistingCarId))
            .thenReturn(Optional.empty());

        CarNotFoundException exception = assertThrows(
            CarNotFoundException.class,
            () -> carServiceImpl.getCarById(nonExistingCarId)
        );

        assertEquals(
            ExceptionMessageConstant.CAR_NOT_FOUND_MESSAGE.formatted(nonExistingCarId),
            exception.getMessage()
        );

        verify(carRepository).findCarByIdAndIsEnabledTrue(nonExistingCarId);
        verifyNoInteractions(carMapper);
    }

    @Test
    void getAllCar_shouldReturnPageResponse() {
        int currentPage = 0;
        int limit = 10;
        PageRequest pageRequest = PageRequest.of(currentPage, limit);

        List<Car> cars = List.of(car);
        Page<Car> carPage = new PageImpl<>(cars, pageRequest, cars.size());

        when(carRepository.findAll(pageRequest)).thenReturn(carPage);
        when(pageResponseMapper.toPageResponse(eq(carPage), eq(currentPage), any()))
            .thenReturn(new PageResponse<>(List.of(carResponse), currentPage, carPage.getTotalElements(),
                carPage.getTotalPages()));

        PageResponse<CarResponse> result = carServiceImpl.getAllCar(currentPage, limit);

        assertNotNull(result);
        assertEquals(cars.size(), result.content().size());
        assertEquals(currentPage, result.currentPage());
        assertEquals(carPage.getTotalPages(), result.totalPages());
        assertEquals(carPage.getTotalElements(), result.totalElements());

        verify(carRepository).findAll(pageRequest);
        verify(pageResponseMapper).toPageResponse(eq(carPage), eq(currentPage), any());
    }

    @Test
    void createCar_shouldReturnCarResponse() {
        CarRequest carRequest = UnitTestDataProvider.carCreateRequest();
        CarResponse carCreateResponse = UnitTestDataProvider.carCreateResponse();
        String carRequestNumber = UnitTestDataProvider.TEST_CAR_NUMBER;

        when(carMapper.toEntity(carRequest)).thenReturn(car);
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.toResponse(car)).thenReturn(carCreateResponse);

        CarResponse result = carServiceImpl.createCar(carRequest);

        assertNotNull(result);
        assertEquals(carCreateResponse, result);

        verify(carValidator).validateCarByNumber(carRequestNumber);
        verify(carMapper).toEntity(carRequest);
        verify(carRepository).save(car);
        verify(carMapper).toResponse(car);
    }

    @Test
    void updateCar_shouldReturnCarResponse() {
        Long existingCarId = car.getId();
        CarRequest updateRequest = UnitTestDataProvider.carUpdateRequest();
        CarResponse updatedResponse = UnitTestDataProvider.carUpdateResponse();

        when(carRepository.findCarByIdAndIsEnabledTrue(existingCarId))
            .thenReturn(Optional.of(car));
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.toResponse(car)).thenReturn(updatedResponse);

        CarResponse result = carServiceImpl.updateCar(existingCarId, updateRequest);

        assertNotNull(result);
        assertEquals(updatedResponse, result);

        verify(carRepository).findCarByIdAndIsEnabledTrue(existingCarId);
        verify(carMapper).updateFromRequest(updateRequest, car);
        verify(carRepository).save(car);
        verify(carMapper).toResponse(car);
    }

    @Test
    void deleteCarById_shouldReturnTrue() {
        Long existingCarId = car.getId();

        when(carRepository.findCarByIdAndIsEnabledTrue(existingCarId))
            .thenReturn(Optional.of(car));

        Boolean result = carServiceImpl.deleteCarById(existingCarId);

        assertTrue(result);
        assertFalse(car.getIsEnabled());

        verify(carRepository).findCarByIdAndIsEnabledTrue(existingCarId);
        verify(carRepository).save(car);
    }

    @Test
    void deleteCarById_shouldThrowNotFoundException() {
        Long nonExistingCarId = 2L;

        when(carRepository.findCarByIdAndIsEnabledTrue(nonExistingCarId))
            .thenReturn(Optional.empty());

        CarNotFoundException exception = assertThrows(
            CarNotFoundException.class,
            () -> carServiceImpl.deleteCarById(nonExistingCarId)
        );

        assertEquals(
            ExceptionMessageConstant.CAR_NOT_FOUND_MESSAGE.formatted(nonExistingCarId),
            exception.getMessage()
        );

        verify(carRepository).findCarByIdAndIsEnabledTrue(nonExistingCarId);
        verifyNoInteractions(carMapper);
        verify(carRepository, never()).save(car);
    }

}