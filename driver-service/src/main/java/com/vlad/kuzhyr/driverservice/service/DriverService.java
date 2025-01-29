package com.vlad.kuzhyr.driverservice.service;

import com.vlad.kuzhyr.driverservice.web.request.DriverRequest;
import com.vlad.kuzhyr.driverservice.web.response.DriverResponse;

import java.util.List;

public interface DriverService {

  DriverResponse getDriverById(Long id);

  List<DriverResponse> getAllDriver(Integer offset, Integer limit);

  DriverResponse createDriver(DriverRequest driverRequest);

  DriverResponse updateDriver(Long id, DriverRequest driverRequest);

  Boolean deleteDriverById(Long id);
}
