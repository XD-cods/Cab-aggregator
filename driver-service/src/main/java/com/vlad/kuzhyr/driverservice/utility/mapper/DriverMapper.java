package com.vlad.kuzhyr.driverservice.utility.mapper;

import com.vlad.kuzhyr.driverservice.persistence.entity.Driver;
import com.vlad.kuzhyr.driverservice.web.response.DriverResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DriverMapper {
  private final ModelMapper modelMapper;

  public DriverResponse mapDriverToDriverResponse(Driver existDriver) {
    return modelMapper.map(existDriver, DriverResponse.class);
  }
}
