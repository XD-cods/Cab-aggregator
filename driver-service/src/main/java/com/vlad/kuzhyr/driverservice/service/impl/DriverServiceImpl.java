package com.vlad.kuzhyr.driverservice.service.impl;

import com.vlad.kuzhyr.driverservice.persistence.repository.DriverRepository;
import com.vlad.kuzhyr.driverservice.service.DriverService;
import com.vlad.kuzhyr.driverservice.utility.mapper.DriverMapper;
import com.vlad.kuzhyr.driverservice.web.request.DriverRequest;
import com.vlad.kuzhyr.driverservice.web.response.DriverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
  private final DriverRepository driverRepository;
  private final DriverMapper driverMapper;

  @Override
  public DriverResponse getDriverById(Long id) {
    return null;
  }

  @Override
  public DriverResponse createDriver(DriverRequest passengerRequest) {
    return null;
  }

  @Override
  public DriverResponse updateDriver(Long id, DriverRequest passengerRequest) {
    return null;
  }

  @Override
  public Boolean deleteDriverById(Long id) {
    return null;
  }
}
