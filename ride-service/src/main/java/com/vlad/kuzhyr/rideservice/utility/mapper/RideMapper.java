package com.vlad.kuzhyr.rideservice.utility.mapper;

import com.vlad.kuzhyr.rideservice.persistence.entity.Ride;
import com.vlad.kuzhyr.rideservice.web.request.RideRequest;
import com.vlad.kuzhyr.rideservice.web.response.RideResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RideMapper {

    @Mapping(source = "departureAddress.addressName", target = "startAddress")
    @Mapping(source = "destinationAddress.addressName", target = "finishAddress")
    RideResponse toResponse(Ride ride);

    @Mapping(source = "destinationAddress", target = "destinationAddress", ignore = true)
    @Mapping(source = "departureAddress", target = "departureAddress", ignore = true)
    Ride toEntity(RideRequest rideRequest);

}