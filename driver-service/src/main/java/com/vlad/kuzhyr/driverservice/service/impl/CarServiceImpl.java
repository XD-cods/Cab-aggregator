package com.vlad.kuzhyr.driverservice.service.impl;

import com.vlad.kuzhyr.driverservice.exception.CarAlreadyExistException;
import com.vlad.kuzhyr.driverservice.exception.CarNotFoundException;
import com.vlad.kuzhyr.driverservice.persistence.entity.Car;
import com.vlad.kuzhyr.driverservice.persistence.repository.CarRepository;
import com.vlad.kuzhyr.driverservice.service.CarService;
import com.vlad.kuzhyr.driverservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.driverservice.utility.mapper.CarMapper;
import com.vlad.kuzhyr.driverservice.web.request.CarRequest;
import com.vlad.kuzhyr.driverservice.web.response.CarResponse;
import com.vlad.kuzhyr.driverservice.web.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
  private final CarRepository carRepository;
  private final CarMapper carMapper;

  @Override
  public CarResponse getCarById(Long id) {
    Car existCar = carRepository.findCarByIdAndIsEnabledTrue(id)
            .orElseThrow(() -> new CarNotFoundException(
                    ExceptionMessageConstant.CAR_NOT_FOUND_MESSAGE.formatted(id)
            ));

    return carMapper.toResponse(existCar);
  }

  @Override
  public PageResponse<CarResponse> getAllCar(Integer offset, Integer limit) {
    Pageable pageable = PageRequest.of(offset, limit);
    Page<Car> carsPage = carRepository.findByIsEnabledTrue(pageable);
    List<CarResponse> carResponse = carsPage.getContent()
            .stream()
            .map(carMapper::toResponse)
            .toList();

    return PageResponse.<CarResponse>builder()
            .content(carResponse)
            .currentOffset(offset)
            .totalElements(carsPage.getTotalElements())
            .totalPages(carsPage.getTotalPages())
            .build();
  }

  @Override
  @Transactional
  public CarResponse createCar(CarRequest carRequest) {
    String carRequestNumber = carRequest.carNumber();

    if (carRepository.existsCarByCarNumberAndIsEnabledTrue(carRequestNumber)) {
      throw new CarAlreadyExistException(
              ExceptionMessageConstant.CAR_ALREADY_EXISTS_BY_CAR_NUMBER_MESSAGE.formatted(carRequestNumber)
      );
    }

    Car newCar = carMapper.toEntity(carRequest);
    Car savedCar = carRepository.save(newCar);
    return carMapper.toResponse(savedCar);
  }

  @Override
  @Transactional
  public CarResponse updateCar(Long id, CarRequest carRequest) {
    Car existCar = carRepository.findCarByIdAndIsEnabledTrue(id)
            .orElseThrow(() -> new CarNotFoundException(
                    ExceptionMessageConstant.CAR_NOT_FOUND_MESSAGE.formatted(id)
            ));

    carMapper.updateFromRequest(carRequest, existCar);
    carRepository.save(existCar);
    return carMapper.toResponse(existCar);
  }

  @Override
  @Transactional
  public Boolean deleteCarById(Long id) {
    Car existCar = carRepository.findCarByIdAndIsEnabledTrue(id)
            .orElseThrow(() -> new CarNotFoundException(
                    ExceptionMessageConstant.CAR_NOT_FOUND_MESSAGE.formatted(id))
            );

    existCar.setDriver(null);
    existCar.setIsEnabled(Boolean.FALSE);
    carRepository.save(existCar);
    return Boolean.TRUE;
  }

}

