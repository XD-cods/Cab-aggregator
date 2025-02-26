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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final PageResponseMapper pageResponseMapper;
    private final CarValidator carValidator;

    @Override
    public CarResponse getCarById(Long id) {
        log.debug("getCarById: Entering method. Car id: {}", id);

        Car existingCar = getExistingCarById(id);

        log.info("getCarById: Car found. Car id: {}", id);
        return carMapper.toResponse(existingCar);
    }

    @Override
    public PageResponse<CarResponse> getAllCar(Integer currentPage, Integer limit) {
        log.debug("getAllCar: Entering method. Current page: {}, limit: {}", currentPage, limit);

        Pageable pageable = PageRequest.of(currentPage, limit);
        Page<Car> carsPage = carRepository.findAll(pageable);

        PageResponse<CarResponse> pageResponse = pageResponseMapper.toPageResponse(
            carsPage,
            currentPage,
            carMapper::toResponse
        );

        log.info("getAllCar: Page of cars retrieved. {}", pageResponse);
        return pageResponse;
    }

    @Override
    @Transactional
    public CarResponse createCar(CarRequest carRequest) {
        log.debug("createCar: Entering method. Car request: {}", carRequest);

        String carRequestNumber = carRequest.carNumber();

        carValidator.validateCarByNumber(carRequestNumber);

        Car newCar = carMapper.toEntity(carRequest);
        Car savedCar = carRepository.save(newCar);

        log.info("createCar: Car created successfully. Car id: {}", savedCar.getId());
        return carMapper.toResponse(savedCar);
    }

    @Override
    @Transactional
    public CarResponse updateCar(Long id, CarRequest carRequest) {
        log.debug("updateCar: Entering method. Car id: {}, car request: {}", id, carRequest);

        Car existingCar = getExistingCarById(id);

        carMapper.updateFromRequest(carRequest, existingCar);
        Car savedCar = carRepository.save(existingCar);

        log.info("updateCar: Car updated successfully. Car id: {}", id);
        return carMapper.toResponse(savedCar);
    }

    @Override
    @Transactional
    public Boolean deleteCarById(Long id) {
        log.debug("deleteCarById: Entering method. Car id: {}", id);

        Car existingCar = getExistingCarById(id);

        existingCar.setDriver(null);
        existingCar.setIsEnabled(Boolean.FALSE);
        carRepository.save(existingCar);

        log.info("deleteCarById: Car deleted successfully. Car id: {}", id);
        return Boolean.TRUE;
    }

    private Car getExistingCarById(Long id) {
        log.debug("getExistingCarById: Attempting to find car. Car id: {}", id);

        return carRepository.findCarByIdAndIsEnabledTrue(id)
            .orElseThrow(() -> {
                log.error("getExistingCarById: Car not found. Car id: {}", id);
                return new CarNotFoundException(
                    ExceptionMessageConstant.CAR_NOT_FOUND_MESSAGE.formatted(id)
                );
            });
    }
}
