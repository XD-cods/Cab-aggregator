package com.vlad.kuzhyr.passengerservice.web.controller.impl.unittest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vlad.kuzhyr.passengerservice.constant.ControllerRouteConstant;
import com.vlad.kuzhyr.passengerservice.constant.UnitTestDataProvider;
import com.vlad.kuzhyr.passengerservice.exception.ControllerAdvice;
import com.vlad.kuzhyr.passengerservice.exception.PassengerNotFoundException;
import com.vlad.kuzhyr.passengerservice.service.PassengerService;
import com.vlad.kuzhyr.passengerservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.passengerservice.web.controller.impl.PassengerControllerImpl;
import com.vlad.kuzhyr.passengerservice.web.dto.request.PassengerRequest;
import com.vlad.kuzhyr.passengerservice.web.dto.response.PageResponse;
import com.vlad.kuzhyr.passengerservice.web.dto.response.PassengerResponse;
import java.util.List;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorFactoryImpl;
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
public class PassengerControllerImplTest {

    @Mock
    private PassengerService passengerService;

    @InjectMocks
    private PassengerControllerImpl passengerController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        validator.setConstraintValidatorFactory(new ConstraintValidatorFactoryImpl());

        mockMvc = MockMvcBuilders.standaloneSetup(passengerController)
            .setControllerAdvice(new ControllerAdvice())
            .setValidator(validator)
            .build();
    }

    @Test
    public void getPassengerById_shouldReturnPassengerResponse() throws Exception {
        Long passengerId = UnitTestDataProvider.TEST_PASSENGER_ID;
        PassengerResponse passengerResponse = UnitTestDataProvider.passengerResponse();

        when(passengerService.getPassengerById(passengerId)).thenReturn(passengerResponse);

        mockMvc.perform(get(ControllerRouteConstant.GET_PASSENGER_BY_ID_URL.formatted(passengerId)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(passengerService).getPassengerById(passengerId);
    }

    @Test
    public void getPassengerById_shouldThrowNotFoundException() throws Exception {
        Long nonExistingPassengerId = -5L;
        String exceptionMessage =
            ExceptionMessageConstant.PASSENGER_NOT_FOUND_MESSAGE.formatted(nonExistingPassengerId);

        when(passengerService.getPassengerById(nonExistingPassengerId))
            .thenThrow(new PassengerNotFoundException(exceptionMessage));

        mockMvc.perform(get(ControllerRouteConstant.GET_PASSENGER_BY_ID_URL.formatted(nonExistingPassengerId)))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(passengerService).getPassengerById(nonExistingPassengerId);
    }

    @Test
    public void getPassengers_shouldReturnPageResponse() throws Exception {
        int currentPage = 0;
        int limit = 10;
        PassengerResponse passengerResponse = UnitTestDataProvider.passengerResponse();

        when(passengerService.getPassengers(currentPage, limit))
            .thenReturn(new PageResponse<>(List.of(passengerResponse), currentPage, 1L, 1));

        mockMvc.perform(get(ControllerRouteConstant.GET_PASSENGERS_URL)
                .param("current_page", String.valueOf(currentPage))
                .param("limit", String.valueOf(limit)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(passengerService).getPassengers(currentPage, limit);
    }

    @Test
    public void createPassenger_shouldReturnPassengerResponse() throws Exception {
        PassengerRequest passengerRequest = UnitTestDataProvider.passengerCreateRequest();
        PassengerResponse passengerResponse = UnitTestDataProvider.passengerCreateResponse();

        when(passengerService.createPassenger(any(PassengerRequest.class))).thenReturn(passengerResponse);

        mockMvc.perform(post(ControllerRouteConstant.CREATE_PASSENGER_URL)
                .content(objectMapper.writeValueAsString(passengerRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(passengerService).createPassenger(any(PassengerRequest.class));
    }

    @Test
    public void createPassenger_shouldReturnBadRequestForInvalidRequest() throws Exception {
        PassengerRequest invalidRequest = UnitTestDataProvider.passengerInvalidRequest();

        mockMvc.perform(post(ControllerRouteConstant.CREATE_PASSENGER_URL)
                .content(objectMapper.writeValueAsString(invalidRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(passengerService, never()).createPassenger(any(PassengerRequest.class));
    }

    @Test
    public void updatePassenger_shouldReturnPassengerResponse() throws Exception {
        Long passengerId = UnitTestDataProvider.TEST_PASSENGER_ID;
        PassengerRequest passengerRequest = UnitTestDataProvider.passengerUpdateRequest();
        PassengerResponse passengerResponse = UnitTestDataProvider.passengerUpdateResponse();

        when(passengerService.updatePassenger(eq(passengerId), any(PassengerRequest.class)))
            .thenReturn(passengerResponse);

        mockMvc.perform(put(ControllerRouteConstant.UPDATE_PASSENGER_URL.formatted(passengerId))
                .content(objectMapper.writeValueAsString(passengerRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(passengerService).updatePassenger(eq(passengerId), any(PassengerRequest.class));
    }

    @Test
    public void deletePassenger_shouldReturnTrue() throws Exception {
        Long passengerId = UnitTestDataProvider.TEST_PASSENGER_ID;

        when(passengerService.deletePassengerById(passengerId)).thenReturn(true);

        mockMvc.perform(delete(ControllerRouteConstant.DELETE_PASSENGER_URL.formatted(passengerId)))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));

        verify(passengerService).deletePassengerById(passengerId);
    }

    @Test
    public void deletePassenger_shouldThrowNotFoundException() throws Exception {
        Long nonExistingPassengerId = 999L;

        when(passengerService.deletePassengerById(nonExistingPassengerId))
            .thenThrow(new PassengerNotFoundException("Passenger not found"));

        mockMvc.perform(delete(ControllerRouteConstant.DELETE_PASSENGER_URL.formatted(nonExistingPassengerId)))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(passengerService).deletePassengerById(nonExistingPassengerId);
    }
}
