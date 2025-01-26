package com.vlad.kuzhyr.driverservice.service.impl;

import com.vlad.kuzhyr.driverservice.persistence.repository.CarRepository;
import com.vlad.kuzhyr.driverservice.service.CarService;
import com.vlad.kuzhyr.driverservice.utility.mapper.CarMapper;
import com.vlad.kuzhyr.driverservice.web.request.CarRequest;
import com.vlad.kuzhyr.driverservice.web.response.CarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
  private final CarRepository carRepository;
  private final CarMapper carMapper;

  @Override
  public CarResponse getCarById(Long id) {
    return null;
  }

  @Override
  public CarResponse createCar(CarRequest passengerRequest) {
    return null;
  }

  @Override
  public CarResponse updateCar(Long id, CarRequest passengerRequest) {
    return null;
  }

  @Override
  public Boolean deleteCarById(Long id) {
    return null;
  }
}
