package com.vlad.kuzhyr.driverservice.service;

import com.vlad.kuzhyr.driverservice.web.request.CarRequest;
import com.vlad.kuzhyr.driverservice.web.response.CarResponse;

public interface CarService {

  CarResponse getCarById(Long id);

  CarResponse createCar(CarRequest passengerRequest);

  CarResponse updateCar(Long id, CarRequest passengerRequest);

  Boolean deleteCarById(Long id);

}
