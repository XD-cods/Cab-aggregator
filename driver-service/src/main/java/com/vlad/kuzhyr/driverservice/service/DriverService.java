package com.vlad.kuzhyr.driverservice.service;

import com.vlad.kuzhyr.driverservice.exception.NotFoundException;
import com.vlad.kuzhyr.driverservice.persistence.entity.Driver;
import com.vlad.kuzhyr.driverservice.persistence.repository.DriverRepository;
import com.vlad.kuzhyr.driverservice.utility.constant.DriverServiceConstant;
import com.vlad.kuzhyr.driverservice.utility.mapper.DriverMapper;
import com.vlad.kuzhyr.driverservice.web.request.DriverRequest;
import com.vlad.kuzhyr.driverservice.web.response.DriverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverService {
  private final DriverRepository driverRepository;
  private final DriverMapper driverMapper;

  public DriverResponse getDriverById(Long driverId) {
    Driver existDriver = driverRepository.findById(driverId)
            .orElseThrow(() -> new NotFoundException(String.format(DriverServiceConstant.NOT_FOUND_MESSAGE, driverId)));

    return driverMapper.mapDriverToDriverResponse(existDriver);
  }

  public DriverResponse createDriver(DriverRequest driverRequest) {

  }
}
