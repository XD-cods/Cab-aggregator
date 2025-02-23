package com.vlad.kuzhyr.passengerservice.service;

import com.vlad.kuzhyr.passengerservice.web.dto.request.PassengerRequest;
import com.vlad.kuzhyr.passengerservice.web.dto.response.PageResponse;
import com.vlad.kuzhyr.passengerservice.web.dto.response.PassengerResponse;

public interface PassengerService {

    PassengerResponse getPassengerById(Long id);

    PassengerResponse createPassenger(PassengerRequest passengerRequest);

    PassengerResponse updatePassenger(Long id, PassengerRequest passengerRequest);

    Boolean deletePassengerById(Long id);

    PageResponse<PassengerResponse> getPassengers(Integer currentPage, Integer limit);

}
