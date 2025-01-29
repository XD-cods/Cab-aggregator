package com.vlad.kuzhyr.driverservice.service;

import com.vlad.kuzhyr.driverservice.web.request.DriverRequest;
import com.vlad.kuzhyr.driverservice.web.request.DriverUpdateCarsRequest;
import com.vlad.kuzhyr.driverservice.web.response.DriverResponse;
import com.vlad.kuzhyr.driverservice.web.response.PageResponse;

public interface DriverService {

  DriverResponse getDriverById(Long id);

  PageResponse<DriverResponse> getAllDriver(Integer offset, Integer limit);

  DriverResponse createDriver(DriverRequest driverRequest);

  DriverResponse updateDriver(Long id, DriverRequest driverRequest);

  DriverResponse updateDriverCarsById(Long id, DriverUpdateCarsRequest driverUpdateCarsRequest);

  Boolean deleteDriverById(Long id);
}
