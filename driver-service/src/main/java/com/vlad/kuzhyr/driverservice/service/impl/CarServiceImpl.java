package com.vlad.kuzhyr.driverservice.service.impl;

import com.vlad.kuzhyr.driverservice.exception.CarNotFoundException;
import com.vlad.kuzhyr.driverservice.persistence.entity.Car;
import com.vlad.kuzhyr.driverservice.persistence.repository.CarRepository;
import com.vlad.kuzhyr.driverservice.service.CarService;
import com.vlad.kuzhyr.driverservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.driverservice.utility.mapper.CarMapper;
import com.vlad.kuzhyr.driverservice.utility.mapper.PageResponseMapper;
import com.vlad.kuzhyr.driverservice.utility.validator.CarValidator;
import com.vlad.kuzhyr.driverservice.web.dto.request.CarRequest;
import com.vlad.kuzhyr.driverservice.web.dto.response.CarResponse;
import com.vlad.kuzhyr.driverservice.web.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final PageResponseMapper pageResponseMapper;
    private final CarValidator carValidator;

    @Override
    public CarResponse getCarById(Long id) {
        Car existCar = getExistingCarById(id);
        return carMapper.toResponse(existCar);
    }

    @Override
    public PageResponse<CarResponse> getAllCar(Integer currentPage, Integer limit) {
        Pageable pageable = PageRequest.of(currentPage, limit);
        Page<Car> carsPage = carRepository.findAll(pageable);

        return pageResponseMapper.toPageResponse(
            carsPage,
            currentPage,
            carMapper::toResponse
        );
    }

    @Override
    @Transactional
    public CarResponse createCar(CarRequest carRequest) {
        String carRequestNumber = carRequest.carNumber();

        carValidator.validateCarByNumber(carRequestNumber);

        Car newCar = carMapper.toEntity(carRequest);
        Car savedCar = carRepository.save(newCar);

        return carMapper.toResponse(savedCar);
    }

    @Override
    @Transactional
    public CarResponse updateCar(Long id, CarRequest carRequest) {
        Car existCar = getExistingCarById(id);

        carMapper.updateFromRequest(carRequest, existCar);
        Car savedCar = carRepository.save(existCar);

        return carMapper.toResponse(savedCar);
    }

    @Override
    @Transactional
    public Boolean deleteCarById(Long id) {
        Car existCar = getExistingCarById(id);

        existCar.setDriver(null);
        existCar.setIsEnabled(Boolean.FALSE);
        carRepository.save(existCar);

        return Boolean.TRUE;
    }

    private Car getExistingCarById(Long id) {
        return carRepository.findCarByIdAndIsEnabledTrue(id)
            .orElseThrow(() -> new CarNotFoundException(
                ExceptionMessageConstant.CAR_NOT_FOUND_MESSAGE.formatted(id)
            ));
    }

}
