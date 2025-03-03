package com.vlad.kuzhyr.driverservice.web.controller.impl.unittest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vlad.kuzhyr.driverservice.constant.ControllerRouteConstant;
import com.vlad.kuzhyr.driverservice.constant.UnitTestDataProvider;
import com.vlad.kuzhyr.driverservice.exception.ControllerAdvice;
import com.vlad.kuzhyr.driverservice.exception.DriverNotFoundException;
import com.vlad.kuzhyr.driverservice.service.DriverService;
import com.vlad.kuzhyr.driverservice.web.controller.impl.DriverControllerImpl;
import com.vlad.kuzhyr.driverservice.web.dto.request.DriverRequest;
import com.vlad.kuzhyr.driverservice.web.dto.request.DriverUpdateCarsRequest;
import com.vlad.kuzhyr.driverservice.web.dto.response.DriverResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
public class DriverControllerImplTest {

    @Mock
    private DriverService driverService;

    @InjectMocks
    private DriverControllerImpl driverController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(driverController)
            .setControllerAdvice(new ControllerAdvice())
            .setValidator(validator)
            .build();
    }

    @Test
    public void getDriverById_shouldReturnDriverResponse() throws Exception {
        Long driverId = UnitTestDataProvider.TEST_ID;
        DriverResponse driverResponse = UnitTestDataProvider.driverResponse();

        when(driverService.getDriverById(driverId)).thenReturn(driverResponse);

        mockMvc.perform(get(ControllerRouteConstant.GET_DRIVER_BY_ID_URL.formatted(driverId)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(driverService).getDriverById(driverId);
    }

    @Test
    public void getDriverById_shouldThrowNotFoundException() throws Exception {
        Long nonExistingDriverId = -5L;

        when(driverService.getDriverById(nonExistingDriverId))
            .thenThrow(new DriverNotFoundException("Driver not found"));

        mockMvc.perform(get(ControllerRouteConstant.GET_DRIVER_BY_ID_URL.formatted(nonExistingDriverId)))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(driverService).getDriverById(nonExistingDriverId);
    }

    @Test
    public void getAllDrivers_shouldReturnPageResponse() throws Exception {
        int currentPage = 0;
        int limit = 10;
        DriverResponse driverResponse = UnitTestDataProvider.driverResponse();

        when(driverService.getAllDriver(currentPage, limit))
            .thenReturn(new PageResponse<>(List.of(driverResponse), currentPage, 1L, 1));

        mockMvc.perform(get(ControllerRouteConstant.GET_ALL_DRIVERS_URL)
                .param("current_page", String.valueOf(currentPage))
                .param("limit", String.valueOf(limit)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(driverService).getAllDriver(currentPage, limit);
    }

    @Test
    public void createDriver_shouldReturnDriverResponse() throws Exception {
        DriverRequest driverRequest = UnitTestDataProvider.driverCreateRequest();
        DriverResponse driverResponse = UnitTestDataProvider.driverCreateResponse();

        when(driverService.createDriver(any(DriverRequest.class))).thenReturn(driverResponse);

        mockMvc.perform(post(ControllerRouteConstant.CREATE_DRIVER_URL)
                .content(objectMapper.writeValueAsString(driverRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(driverService).createDriver(any(DriverRequest.class));
    }

    @Test
    public void createDriver_shouldReturnBadRequestForInvalidRequest() throws Exception {
        DriverRequest invalidRequest = UnitTestDataProvider.driverInvalidRequest();

        mockMvc.perform(post(ControllerRouteConstant.CREATE_DRIVER_URL)
                .content(objectMapper.writeValueAsString(invalidRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(driverService, never()).createDriver(any(DriverRequest.class));
    }

    @Test
    public void updateDriver_shouldReturnDriverResponse() throws Exception {
        Long driverId = UnitTestDataProvider.TEST_ID;
        DriverRequest driverRequest = UnitTestDataProvider.driverUpdateRequest();
        DriverResponse driverResponse = UnitTestDataProvider.driverUpdateResponse();

        when(driverService.updateDriver(eq(driverId), any(DriverRequest.class)))
            .thenReturn(driverResponse);

        mockMvc.perform(put(ControllerRouteConstant.UPDATE_DRIVER_URL.formatted(driverId))
                .content(objectMapper.writeValueAsString(driverRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(driverService).updateDriver(eq(driverId), any(DriverRequest.class));
    }

    @Test
    public void updateDriverCars_shouldReturnDriverResponse() throws Exception {
        Long driverId = UnitTestDataProvider.TEST_ID;
        DriverUpdateCarsRequest driverUpdateCarsRequest = UnitTestDataProvider.driverUpdateCarsRequest();
        DriverResponse driverResponse = UnitTestDataProvider.driverUpdateCarsResponse();

        when(driverService.updateDriverCarsById(eq(driverId), any(DriverUpdateCarsRequest.class)))
            .thenReturn(driverResponse);

        mockMvc.perform(patch(ControllerRouteConstant.UPDATE_DRIVER_CARS_URL.formatted(driverId))
                .content(objectMapper.writeValueAsString(driverUpdateCarsRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(driverService).updateDriverCarsById(eq(driverId), any(DriverUpdateCarsRequest.class));
    }

    @Test
    public void deleteDriver_shouldReturnTrue() throws Exception {
        Long driverId = UnitTestDataProvider.TEST_ID;

        when(driverService.deleteDriverById(driverId)).thenReturn(true);

        mockMvc.perform(delete(ControllerRouteConstant.DELETE_DRIVER_URL.formatted(driverId)))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));

        verify(driverService).deleteDriverById(driverId);
    }

    @Test
    public void deleteDriver_shouldThrowNotFoundException() throws Exception {
        Long nonExistingDriverId = 999L;

        when(driverService.deleteDriverById(nonExistingDriverId))
            .thenThrow(new DriverNotFoundException("Driver not found"));

        mockMvc.perform(delete(ControllerRouteConstant.DELETE_DRIVER_URL.formatted(nonExistingDriverId)))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(driverService).deleteDriverById(nonExistingDriverId);
    }

}
