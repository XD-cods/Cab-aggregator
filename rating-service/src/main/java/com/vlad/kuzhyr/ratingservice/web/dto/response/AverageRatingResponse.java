package com.vlad.kuzhyr.ratingservice.web.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public record AverageRatingResponse(

    Double averageRating

) {
    @Override
    public String toString() {
        return "AverageRatingResponse{" +
               "averageRating=" + averageRating +
               '}';
    }
}
