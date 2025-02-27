package com.vlad.kuzhyr.ratingservice.utility.mapper;

import com.vlad.kuzhyr.ratingservice.persistence.entity.RideInfo;
import com.vlad.kuzhyr.ratingservice.web.dto.external.RideInfoPayload;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface RideInfoMapper {

    RideInfo toEntity(RideInfoPayload rideInfo);

}
