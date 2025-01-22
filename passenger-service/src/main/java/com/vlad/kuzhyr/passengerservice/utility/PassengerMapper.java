package com.vlad.kuzhyr.passengerservice.utility;

import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import com.vlad.kuzhyr.passengerservice.web.request.PassengerRequest;
import com.vlad.kuzhyr.passengerservice.web.response.PassengerResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PassengerMapper {
  private final ModelMapper modelMapper;

  public PassengerResponse mapPassengerToPassengerResponse(Passenger passenger) {
    return modelMapper.map(passenger, PassengerResponse.class);
  }

  public void updateExistPassengerFromPassengerRequest(Passenger existPassenger, PassengerRequest passengerRequest) {
    modelMapper.map(passengerRequest, existPassenger);
  }
}