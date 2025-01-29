package com.vlad.kuzhyr.driverservice.web.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.util.List;

@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PageResponse<T>(

        List<T> content,
        Integer currentOffset,
        Long totalElements,
        Integer totalPages

) {
}