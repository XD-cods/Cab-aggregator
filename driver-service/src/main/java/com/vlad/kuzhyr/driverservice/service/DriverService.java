package com.vlad.kuzhyr.driverservice.service;

import com.vlad.kuzhyr.driverservice.web.dto.request.DriverRequest;
import com.vlad.kuzhyr.driverservice.web.dto.request.DriverUpdateCarsRequest;
import com.vlad.kuzhyr.driverservice.web.dto.response.DriverResponse;
import com.vlad.kuzhyr.driverservice.web.dto.response.PageResponse;

public interface DriverService {

    DriverResponse getDriverById(Long id);

    PageResponse<DriverResponse> getAllDriver(Integer currentPage, Integer limit);

    DriverResponse createDriver(DriverRequest driverRequest);

    DriverResponse updateDriver(Long id, DriverRequest driverRequest);

    DriverResponse updateDriverCarsById(Long id, DriverUpdateCarsRequest driverUpdateCarsRequest);

    Boolean deleteDriverById(Long id);

}
