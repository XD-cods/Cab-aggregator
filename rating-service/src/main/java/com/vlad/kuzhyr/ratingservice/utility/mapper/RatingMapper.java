package com.vlad.kuzhyr.ratingservice.utility.mapper;

import com.vlad.kuzhyr.ratingservice.persistence.entity.Rating;
import com.vlad.kuzhyr.ratingservice.web.request.CreateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.request.UpdateRatingRequest;
import com.vlad.kuzhyr.ratingservice.web.response.RatingResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface RatingMapper {

    RatingResponse toResponse(Rating rating);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRequest(UpdateRatingRequest updateRatingRequest, @MappingTarget Rating existingRating);

    Rating toEntity(CreateRatingRequest carRequest);

}
