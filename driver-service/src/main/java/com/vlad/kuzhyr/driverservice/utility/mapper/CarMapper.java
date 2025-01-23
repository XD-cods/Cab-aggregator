package com.vlad.kuzhyr.driverservice.utility.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarMapper {
  private final ModelMapper modelMapper;
}
