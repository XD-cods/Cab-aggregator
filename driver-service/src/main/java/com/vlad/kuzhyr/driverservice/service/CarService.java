package com.vlad.kuzhyr.driverservice.service;

import com.vlad.kuzhyr.driverservice.web.request.CarRequest;
import com.vlad.kuzhyr.driverservice.web.response.CarResponse;
import com.vlad.kuzhyr.driverservice.web.response.PageResponse;

public interface CarService {

    CarResponse getCarById(Long id);

    PageResponse<CarResponse> getAllCar(Integer offset, Integer limit);

    CarResponse createCar(CarRequest carRequest);

    CarResponse updateCar(Long id, CarRequest carRequest);

    Boolean deleteCarById(Long id);

}
