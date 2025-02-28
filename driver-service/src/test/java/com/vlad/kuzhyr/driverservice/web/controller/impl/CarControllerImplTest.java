package com.vlad.kuzhyr.driverservice.web.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vlad.kuzhyr.driverservice.constant.ControllerRouteConstant;
import com.vlad.kuzhyr.driverservice.constant.UnitTestDataProvider;
import com.vlad.kuzhyr.driverservice.exception.CarNotFoundException;
import com.vlad.kuzhyr.driverservice.exception.ControllerAdvice;
import com.vlad.kuzhyr.driverservice.service.CarService;
import com.vlad.kuzhyr.driverservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.driverservice.web.dto.request.CarRequest;
import com.vlad.kuzhyr.driverservice.web.dto.response.CarResponse;
import com.vlad.kuzhyr.driverservice.web.dto.response.PageResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
public class CarControllerImplTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private CarControllerImpl carController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(carController)
            .setControllerAdvice(new ControllerAdvice())
            .setValidator(validator)
            .build();
    }

    @Test
    public void getCarById_shouldReturnCarResponse() throws Exception {
        Long carId = UnitTestDataProvider.TEST_ID;
        CarResponse carResponse = UnitTestDataProvider.carResponse();

        when(carService.getCarById(carId)).thenReturn(carResponse);

        mockMvc.perform(get(ControllerRouteConstant.GET_CAR_BY_ID_URL.formatted(carId)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(carService).getCarById(carId);
    }

    @Test
    public void getCarById_shouldThrowNotFoundException() throws Exception {
        Long nonExistingCarId = -5L;

        when(carService.getCarById(nonExistingCarId))
            .thenThrow(new CarNotFoundException(
                ExceptionMessageConstant.CAR_NOT_FOUND_MESSAGE.formatted(nonExistingCarId)
            ));

        mockMvc.perform(get(ControllerRouteConstant.GET_CAR_BY_ID_URL.formatted(nonExistingCarId)))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(carService).getCarById(nonExistingCarId);
    }

    @Test
    public void getAllCars_shouldReturnPageResponse() throws Exception {
        int currentPage = 0;
        int limit = 10;
        CarResponse carResponse = UnitTestDataProvider.carResponse();

        when(carService.getAllCar(currentPage, limit))
            .thenReturn(new PageResponse<>(List.of(carResponse), currentPage, 1L, 1));

        mockMvc.perform(get(ControllerRouteConstant.GET_ALL_CAR_URL)
                .param("current_page", String.valueOf(currentPage))
                .param("limit", String.valueOf(limit)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(carService).getAllCar(currentPage, limit);
    }

    @Test
    public void createCar_shouldReturnCarResponse() throws Exception {
        CarRequest carRequest = UnitTestDataProvider.carCreateRequest();
        CarResponse carResponse = UnitTestDataProvider.carCreateResponse();

        when(carService.createCar(any(CarRequest.class))).thenReturn(carResponse);

        mockMvc.perform(post(ControllerRouteConstant.CREATE_CAR_URL)
                .content(objectMapper.writeValueAsString(carRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(carService).createCar(any(CarRequest.class));
    }

    @Test
    public void createCar_shouldReturnBadRequestForInvalidRequest() throws Exception {
        CarRequest invalidRequest = UnitTestDataProvider.carInvalidRequest();

        mockMvc.perform(post(ControllerRouteConstant.CREATE_CAR_URL)
                .content(objectMapper.writeValueAsString(invalidRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(carService, never()).createCar(any(CarRequest.class));
    }

    @Test
    public void updateCar_shouldReturnCarResponse() throws Exception {
        Long carId = UnitTestDataProvider.TEST_ID;
        CarRequest carRequest = UnitTestDataProvider.carUpdateRequest();
        CarResponse carResponse = UnitTestDataProvider.carUpdateResponse();

        when(carService.updateCar(eq(carId), any(CarRequest.class)))
            .thenReturn(carResponse);

        mockMvc.perform(put(ControllerRouteConstant.UPDATE_CAR_URL.formatted(carId))
                .content(objectMapper.writeValueAsString(carRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(carService).updateCar(eq(carId), any(CarRequest.class));
    }

    @Test
    public void deleteCar_shouldReturnTrue() throws Exception {
        Long carId = UnitTestDataProvider.TEST_ID;

        when(carService.deleteCarById(carId)).thenReturn(true);
        mockMvc.perform(delete(ControllerRouteConstant.DELETE_CAR_URL.formatted(carId)))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));

        verify(carService).deleteCarById(carId);
    }

    @Test
    public void deleteCar_shouldThrowNotFoundException() throws Exception {
        Long nonExistingCarId = 999L;

        when(carService.deleteCarById(nonExistingCarId))
            .thenThrow(new CarNotFoundException(
                ExceptionMessageConstant.CAR_NOT_FOUND_MESSAGE.formatted(nonExistingCarId)
            ));

        mockMvc.perform(delete(ControllerRouteConstant.DELETE_CAR_URL.formatted(nonExistingCarId)))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(carService).deleteCarById(nonExistingCarId);
    }
}
