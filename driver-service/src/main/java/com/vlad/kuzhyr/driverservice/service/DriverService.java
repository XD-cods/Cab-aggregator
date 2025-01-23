package com.vlad.kuzhyr.driverservice.service;

import com.vlad.kuzhyr.driverservice.persistence.repository.DriverRepository;
import com.vlad.kuzhyr.driverservice.utility.mapper.DriverMapper;
import com.vlad.kuzhyr.driverservice.web.response.DriverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverService {
  private final DriverRepository driverRepository;
  private final DriverMapper driverMapper;


  public DriverResponse getDriverById(long l) {
  }
}
