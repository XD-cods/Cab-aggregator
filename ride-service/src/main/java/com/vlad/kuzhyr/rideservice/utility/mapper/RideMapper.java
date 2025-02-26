package com.vlad.kuzhyr.rideservice.utility.mapper;

import com.vlad.kuzhyr.rideservice.persistence.entity.Ride;
import com.vlad.kuzhyr.rideservice.web.dto.request.RideRequest;
import com.vlad.kuzhyr.rideservice.web.dto.response.RideResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface RideMapper {

    @Mapping(source = "departureAddress.addressName", target = "departureAddress")
    @Mapping(source = "destinationAddress.addressName", target = "destinationAddress")
    RideResponse toResponse(Ride ride);

    @Mapping(source = "destinationAddress", target = "destinationAddress", ignore = true)
    @Mapping(source = "departureAddress", target = "departureAddress", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rideDistance", ignore = true)
    @Mapping(target = "pickupTime", ignore = true)
    @Mapping(target = "completeTime", ignore = true)
    @Mapping(target = "orderCreateTime", ignore = true)
    @Mapping(target = "rideStatus", ignore = true)
    @Mapping(target = "ridePrice", ignore = true)
    Ride toEntity(RideRequest rideRequest);

}
