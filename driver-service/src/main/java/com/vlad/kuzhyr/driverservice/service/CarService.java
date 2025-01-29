package com.vlad.kuzhyr.driverservice.service;

import com.vlad.kuzhyr.driverservice.web.request.CarRequest;
import com.vlad.kuzhyr.driverservice.web.response.CarResponse;

import java.util.List;

public interface CarService {

  CarResponse getCarById(Long id);

  List<CarResponse> getAllCar(Integer offset, Integer limit);

  CarResponse createCar(CarRequest carRequest);

  CarResponse updateCar(Long id, CarRequest carRequest);

  Boolean deleteCarById(Long id);
}
