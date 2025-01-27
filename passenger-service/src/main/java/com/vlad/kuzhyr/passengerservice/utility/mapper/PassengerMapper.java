package com.vlad.kuzhyr.passengerservice.utility.mapper;

import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import com.vlad.kuzhyr.passengerservice.web.request.PassengerRequest;
import com.vlad.kuzhyr.passengerservice.web.response.PassengerResponse;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PassengerMapper {

  PassengerResponse toResponse(Passenger passenger);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateFromRequest(PassengerRequest passengerRequest, @MappingTarget Passenger existPassenger);

  Passenger toEntity(PassengerRequest passengerRequest);

}