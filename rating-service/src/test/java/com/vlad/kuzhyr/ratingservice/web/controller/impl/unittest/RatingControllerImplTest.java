package com.vlad.kuzhyr.ratingservice.web.controller.impl.unittest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vlad.kuzhyr.ratingservice.constant.ControllerRouteConstant;
import com.vlad.kuzhyr.ratingservice.constant.UnitTestDataProvider;
import com.vlad.kuzhyr.ratingservice.exception.ControllerAdvice;
import com.vlad.kuzhyr.ratingservice.exception.RatingNotFoundException;
import com.vlad.kuzhyr.ratingservice.service.RatingService;
import com.vlad.kuzhyr.ratingservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.ratingservice.web.controller.ipml.RatingControllerImpl;
import com.vlad.kuzhyr.ratingservice.web.dto.request.CreateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.dto.request.UpdateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.dto.response.AverageRatingResponse;
import com.vlad.kuzhyr.ratingservice.web.dto.response.PageResponse;
import com.vlad.kuzhyr.ratingservice.web.dto.response.RatingResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
public class RatingControllerImplTest {

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private RatingControllerImpl ratingController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(ratingController)
            .setControllerAdvice(new ControllerAdvice())
            .setValidator(validator)
            .build();
    }

    @Test
    void getRatingByRatingId_shouldReturnRatingResponse() throws Exception {
        Long ratingId = UnitTestDataProvider.TEST_ID;
        RatingResponse ratingResponse = UnitTestDataProvider.ratingResponse();

        when(ratingService.getRatingByRatingId(ratingId)).thenReturn(ratingResponse);

        mockMvc.perform(get(ControllerRouteConstant.GET_RATING_BY_ID_URL.formatted(ratingId)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(ratingResponse)));

        verify(ratingService).getRatingByRatingId(ratingId);
    }

    @Test
    void getRatingByRatingId_shouldThrowNotFoundException() throws Exception {
        Long nonExistingRatingId = 999L;

        when(ratingService.getRatingByRatingId(nonExistingRatingId))
            .thenThrow(new RatingNotFoundException(
                ExceptionMessageConstant.RATING_NOT_FOUND_MESSAGE.formatted(nonExistingRatingId)
            ));

        mockMvc.perform(get(ControllerRouteConstant.GET_RATING_BY_ID_URL.formatted(nonExistingRatingId)))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(ratingService).getRatingByRatingId(nonExistingRatingId);
    }

    @Test
    void getRatings_shouldReturnPageResponse() throws Exception {
        int currentPage = 0;
        int limit = 10;
        PageResponse<RatingResponse> pageResponse = new PageResponse<>(
            List.of(UnitTestDataProvider.ratingResponse()),
            currentPage,
            1L,
            1
        );

        when(ratingService.getRatings(currentPage, limit)).thenReturn(pageResponse);

        mockMvc.perform(get(ControllerRouteConstant.GET_RATINGS_URL)
                .param("current_page", String.valueOf(currentPage))
                .param("limit", String.valueOf(limit)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(pageResponse)));

        verify(ratingService).getRatings(currentPage, limit);
    }

    @Test
    void getAverageRatingByPassengerId_shouldReturnAverageRatingResponse() throws Exception {
        Long passengerId = UnitTestDataProvider.TEST_PASSENGER_ID;
        AverageRatingResponse averageRatingResponse = UnitTestDataProvider.averageRatingResponse();

        when(ratingService.getAverageRatingByPassengerId(passengerId)).thenReturn(averageRatingResponse);

        mockMvc.perform(get(ControllerRouteConstant.GET_AVERAGE_RATING_BY_PASSENGER_ID_URL.formatted(passengerId)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(averageRatingResponse)));

        verify(ratingService).getAverageRatingByPassengerId(passengerId);
    }

    @Test
    void getAverageRatingByDriverId_shouldReturnAverageRatingResponse() throws Exception {
        Long driverId = UnitTestDataProvider.TEST_DRIVER_ID;
        AverageRatingResponse averageRatingResponse = UnitTestDataProvider.averageRatingResponse();

        when(ratingService.getAverageRatingByDriverId(driverId)).thenReturn(averageRatingResponse);

        mockMvc.perform(get(ControllerRouteConstant.GET_AVERAGE_RATING_BY_DRIVER_ID_URL.formatted(driverId)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(averageRatingResponse)));

        verify(ratingService).getAverageRatingByDriverId(driverId);
    }

    @Test
    void createRating_shouldReturnRatingResponse() throws Exception {
        CreateRatingRequest createRatingRequest = UnitTestDataProvider.createRatingRequest();
        RatingResponse ratingResponse = UnitTestDataProvider.ratingResponse();

        when(ratingService.createRating(any(CreateRatingRequest.class))).thenReturn(ratingResponse);

        mockMvc.perform(post(ControllerRouteConstant.CREATE_RATING_URL)
                .content(objectMapper.writeValueAsString(createRatingRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(ratingResponse)));

        verify(ratingService).createRating(any(CreateRatingRequest.class));
    }

    @Test
    void updateRating_shouldReturnRatingResponse() throws Exception {
        Long ratingId = UnitTestDataProvider.TEST_ID;
        UpdateRatingRequest updateRatingRequest = UnitTestDataProvider.updateRatingRequest();
        RatingResponse ratingResponse = UnitTestDataProvider.ratingResponse();

        when(ratingService.updateRating(eq(ratingId), any(UpdateRatingRequest.class))).thenReturn(ratingResponse);

        mockMvc.perform(put(ControllerRouteConstant.UPDATE_RATING_URL.formatted(ratingId))
                .content(objectMapper.writeValueAsString(updateRatingRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(ratingResponse)));

        verify(ratingService).updateRating(eq(ratingId), any(UpdateRatingRequest.class));
    }
}