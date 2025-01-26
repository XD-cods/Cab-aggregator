package com.vlad.kuzhyr.driverservice.service;

import com.vlad.kuzhyr.driverservice.web.request.DriverRequest;
import com.vlad.kuzhyr.driverservice.web.response.DriverResponse;

public interface DriverService {

  DriverResponse getDriverById(Long id);

  DriverResponse createDriver(DriverRequest passengerRequest);

  DriverResponse updateDriver(Long id, DriverRequest passengerRequest);

  Boolean deleteDriverById(Long id);

}
