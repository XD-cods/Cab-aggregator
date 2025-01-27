package com.vlad.kuzhyr.driverservice.utility.mapper;

import com.vlad.kuzhyr.driverservice.persistence.entity.Car;
import com.vlad.kuzhyr.driverservice.persistence.entity.Driver;
import com.vlad.kuzhyr.driverservice.web.request.DriverRequest;
import com.vlad.kuzhyr.driverservice.web.response.DriverResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface DriverMapper {

  @Mapping(source = "cars", target = "carIds", qualifiedByName = "mapCarIds")
  DriverResponse toResponse(Driver driver);

  @Mapping(target = "cars", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateFromRequest(DriverRequest driverRequest, @MappingTarget Driver existingDriver);

  @Mapping(target = "cars", ignore = true)
  Driver toEntity(DriverRequest driverRequest);

  @Named("mapCarIds")
  default List<Long> mapCarIds(List<Car> cars) {
    return cars == null || cars.isEmpty() ? new ArrayList<>() : cars.stream().map(Car::getId).toList();
  }
}

