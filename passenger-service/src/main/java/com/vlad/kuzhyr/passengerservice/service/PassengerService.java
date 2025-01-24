package com.vlad.kuzhyr.passengerservice.service;

import com.vlad.kuzhyr.passengerservice.web.request.PassengerRequest;
import com.vlad.kuzhyr.passengerservice.web.response.PassengerResponse;

public interface PassengerService {

  PassengerResponse getPassengerById(Long id);

  PassengerResponse createPassenger(PassengerRequest passengerRequest);

  PassengerResponse updatePassenger(Long id, PassengerRequest passengerRequest);

  Boolean deletePassengerById(Long id);

}
