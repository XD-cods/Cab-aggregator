package com.vlad.kuzhyr.rideservice.controller.unittest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vlad.kuzhyr.rideservice.constant.ControllerRouteConstant;
import com.vlad.kuzhyr.rideservice.constant.UnitTestDataProvider;
import com.vlad.kuzhyr.rideservice.exception.ControllerAdvice;
import com.vlad.kuzhyr.rideservice.exception.RideNotFoundException;
import com.vlad.kuzhyr.rideservice.service.RideService;
import com.vlad.kuzhyr.rideservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.rideservice.web.controller.impl.RideControllerImpl;
import com.vlad.kuzhyr.rideservice.web.dto.request.RideRequest;
import com.vlad.kuzhyr.rideservice.web.dto.request.UpdateRideRequest;
import com.vlad.kuzhyr.rideservice.web.dto.request.UpdateRideStatusRequest;
import com.vlad.kuzhyr.rideservice.web.dto.response.PageResponse;
import com.vlad.kuzhyr.rideservice.web.dto.response.RideResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
public class RideControllerImplTest {

    @Mock
    private RideService rideService;

    @InjectMocks
    private RideControllerImpl rideController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(rideController)
            .setControllerAdvice(new ControllerAdvice(objectMapper))
            .setValidator(validator)
            .build();
    }

    @Test
    void getRideById_shouldReturnRideResponse() throws Exception {
        Long rideId = UnitTestDataProvider.TEST_ID;
        RideResponse rideResponse = UnitTestDataProvider.rideResponse();

        when(rideService.getRideById(rideId)).thenReturn(rideResponse);

        mockMvc.perform(get(ControllerRouteConstant.GET_RIDE_BY_ID_URL.formatted(rideId)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(rideResponse)));

        verify(rideService).getRideById(rideId);
    }

    @Test
    void getRideById_shouldThrowNotFoundException() throws Exception {
        Long nonExistingRideId = 999L;

        when(rideService.getRideById(nonExistingRideId))
            .thenThrow(new RideNotFoundException(
                ExceptionMessageConstant.RIDE_NOT_FOUND_MESSAGE.formatted(nonExistingRideId)
            ));

        mockMvc.perform(get(ControllerRouteConstant.GET_RIDE_BY_ID_URL.formatted(nonExistingRideId)))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(rideService).getRideById(nonExistingRideId);
    }

    @Test
    void getAllRides_shouldReturnPageResponse() throws Exception {
        int currentPage = 0;
        int limit = 10;
        PageResponse<RideResponse> pageResponse = new PageResponse<>(
            List.of(UnitTestDataProvider.rideResponse()),
            currentPage,
            1L,
            1
        );

        when(rideService.getAllRides(currentPage, limit)).thenReturn(pageResponse);

        mockMvc.perform(get(ControllerRouteConstant.GET_ALL_RIDES_URL)
                .param("current_page", String.valueOf(currentPage))
                .param("limit", String.valueOf(limit)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(pageResponse)));

        verify(rideService).getAllRides(currentPage, limit);
    }

    @Test
    void getAllRidesByDriverId_shouldReturnPageResponse() throws Exception {
        Long driverId = UnitTestDataProvider.TEST_DRIVER_ID;
        int currentPage = 0;
        int limit = 10;
        PageResponse<RideResponse> pageResponse = new PageResponse<>(
            List.of(UnitTestDataProvider.rideResponse()),
            currentPage,
            1L,
            1
        );

        when(rideService.getAllRidesByDriverId(driverId, currentPage, limit)).thenReturn(pageResponse);

        mockMvc.perform(get(ControllerRouteConstant.GET_ALL_RIDES_BY_DRIVER_ID_URL.formatted(driverId))
                .param("current_page", String.valueOf(currentPage))
                .param("limit", String.valueOf(limit)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(pageResponse)));

        verify(rideService).getAllRidesByDriverId(driverId, currentPage, limit);
    }

    @Test
    void getAllRidesByPassengerId_shouldReturnPageResponse() throws Exception {
        Long passengerId = UnitTestDataProvider.TEST_PASSENGER_ID;
        int currentPage = 0;
        int limit = 10;
        PageResponse<RideResponse> pageResponse = new PageResponse<>(
            List.of(UnitTestDataProvider.rideResponse()),
            currentPage,
            1L,
            1
        );

        when(rideService.getAllRidesByPassengerId(passengerId, currentPage, limit)).thenReturn(pageResponse);

        mockMvc.perform(get(ControllerRouteConstant.GET_ALL_RIDES_BY_PASSENGER_ID_URL.formatted(passengerId))
                .param("current_page", String.valueOf(currentPage))
                .param("limit", String.valueOf(limit)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(pageResponse)));

        verify(rideService).getAllRidesByPassengerId(passengerId, currentPage, limit);
    }

    @Test
    void createRide_shouldReturnRideResponse() throws Exception {
        RideRequest rideRequest = UnitTestDataProvider.rideRequest();
        RideResponse rideResponse = UnitTestDataProvider.rideResponse();

        when(rideService.createRide(any(RideRequest.class))).thenReturn(rideResponse);

        mockMvc.perform(post(ControllerRouteConstant.CREATE_RIDE_URL)
                .content(objectMapper.writeValueAsString(rideRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(rideResponse)));

        verify(rideService).createRide(any(RideRequest.class));
    }

    @Test
    void updateRide_shouldReturnRideResponse() throws Exception {
        Long rideId = UnitTestDataProvider.TEST_ID;
        UpdateRideRequest updateRideRequest = UnitTestDataProvider.updateRideRequest();
        RideResponse rideResponse = UnitTestDataProvider.rideResponse();

        when(rideService.updateRide(eq(rideId), any(UpdateRideRequest.class))).thenReturn(rideResponse);

        mockMvc.perform(put(ControllerRouteConstant.UPDATE_RIDE_URL.formatted(rideId))
                .content(objectMapper.writeValueAsString(updateRideRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(rideResponse)));

        verify(rideService).updateRide(eq(rideId), any(UpdateRideRequest.class));
    }

    @Test
    void updateRideStatus_shouldReturnRideResponse() throws Exception {
        Long rideId = UnitTestDataProvider.TEST_ID;
        UpdateRideStatusRequest updateRideStatusRequest = UnitTestDataProvider.updateRideStatusRequest();
        RideResponse rideResponse = UnitTestDataProvider.rideResponse();

        when(rideService.updateRideStatus(eq(rideId), any(UpdateRideStatusRequest.class))).thenReturn(rideResponse);

        mockMvc.perform(patch(ControllerRouteConstant.UPDATE_RIDE_STATUS_URL.formatted(rideId))
                .content(objectMapper.writeValueAsString(updateRideStatusRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(rideResponse)));

        verify(rideService).updateRideStatus(eq(rideId), any(UpdateRideStatusRequest.class));
    }
}