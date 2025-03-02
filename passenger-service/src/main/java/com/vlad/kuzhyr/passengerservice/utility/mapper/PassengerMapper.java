package com.vlad.kuzhyr.passengerservice.utility.mapper;

import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import com.vlad.kuzhyr.passengerservice.web.dto.request.PassengerRequest;
import com.vlad.kuzhyr.passengerservice.web.dto.response.PassengerResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface PassengerMapper {

    @Mapping(source = "isEnabled", target = "isEnabled")
    PassengerResponse toResponse(Passenger passenger);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "isEnabled", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateFromRequest(PassengerRequest passengerRequest, @MappingTarget Passenger existingPassenger);

    @Mapping(target = "isEnabled", ignore = true)
    @Mapping(target = "id", ignore = true)
    Passenger toEntity(PassengerRequest passengerRequest);

}
