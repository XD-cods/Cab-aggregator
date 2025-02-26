package com.vlad.kuzhyr.rideservice.web.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Builder;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PageResponse<T>(

    List<T> content,
    Integer currentPage,
    Long totalElements,
    Integer totalPages

) {
}
