package com.vlad.kuzhyr.driverservice.service;

import com.vlad.kuzhyr.driverservice.web.dto.request.CarRequest;
import com.vlad.kuzhyr.driverservice.web.dto.response.CarResponse;
import com.vlad.kuzhyr.driverservice.web.dto.response.PageResponse;

public interface CarService {

    CarResponse getCarById(Long id);

    PageResponse<CarResponse> getAllCar(Integer currentPage, Integer limit);

    CarResponse createCar(CarRequest carRequest);

    CarResponse updateCar(Long id, CarRequest carRequest);

    Boolean deleteCarById(Long id);

}
