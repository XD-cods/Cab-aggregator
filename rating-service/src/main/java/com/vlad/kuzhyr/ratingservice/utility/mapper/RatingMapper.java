package com.vlad.kuzhyr.ratingservice.utility.mapper;

import com.vlad.kuzhyr.ratingservice.persistence.entity.Rating;
import com.vlad.kuzhyr.ratingservice.web.dto.request.CreateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.dto.request.UpdateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.dto.response.RatingResponse;
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
public interface RatingMapper {

    @Mapping(source = "rideInfo", target = "rideInfo")
    RatingResponse toResponse(Rating ride);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rideInfo", ignore = true)
    Rating toEntity(CreateRatingRequest rideRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ratedBy", ignore = true)
    @Mapping(target = "rideInfo", ignore = true)
    void updateFromRequest(UpdateRatingRequest rideRequest, @MappingTarget Rating ride);
}
