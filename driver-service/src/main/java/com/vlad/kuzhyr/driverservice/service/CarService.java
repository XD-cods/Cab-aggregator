package com.vlad.kuzhyr.driverservice.service;

import com.vlad.kuzhyr.driverservice.web.request.CarRequest;
import com.vlad.kuzhyr.driverservice.web.response.CarResponse;

public interface CarService {

  CarResponse getCarById(Long id);

  CarResponse createCar(CarRequest carRequest);

  CarResponse updateCar(Long id, CarRequest carRequest);

  Boolean deleteCarById(Long id);

}
