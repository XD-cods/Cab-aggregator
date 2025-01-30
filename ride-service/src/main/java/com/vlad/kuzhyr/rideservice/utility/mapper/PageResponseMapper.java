package com.vlad.kuzhyr.rideservice.utility.mapper;

import com.vlad.kuzhyr.rideservice.web.response.PageResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PageResponseMapper {

  default <T, R> PageResponse<R> toPageResponse(Page<T> sourcePage, int offset, Function<T, R> mapper) {
    List<R> content = sourcePage.getContent()
            .stream()
            .map(mapper)
            .collect(Collectors.toList());

    return PageResponse.<R>builder()
            .content(content)
            .currentOffset(offset)
            .totalElements(sourcePage.getTotalElements())
            .totalPages(sourcePage.getTotalPages())
            .build();
  }

}
