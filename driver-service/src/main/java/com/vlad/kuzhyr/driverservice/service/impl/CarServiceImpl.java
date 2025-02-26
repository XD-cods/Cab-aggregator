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
        Car existCar = getExistingCarById(id);

        log.info("CarServiceImpl. Get car by id. car id: {}", id);
        return carMapper.toResponse(existCar);
    }

    @Override
    public PageResponse<CarResponse> getAllCar(Integer currentPage, Integer limit) {
        Pageable pageable = PageRequest.of(currentPage, limit);
        Page<Car> carsPage = carRepository.findAll(pageable);

        PageResponse<CarResponse> pageResponse = pageResponseMapper.toPageResponse(
            carsPage,
            currentPage,
            carMapper::toResponse
        );

        log.info("CarServiceImpl. Get all cars. current page: {}, total pages: {}", pageResponse.currentPage(),
            pageResponse.totalPages());
        return pageResponse;
    }

    @Override
    @Transactional
    public CarResponse createCar(CarRequest carRequest) {
        log.debug("CarServiceImpl. Entering in method createCar.");
        String carRequestNumber = carRequest.carNumber();

        log.debug("CarServiceImpl. Entering in method createCar.");
        carValidator.validateCarByNumber(carRequestNumber);

        Car newCar = carMapper.toEntity(carRequest);
        Car savedCar = carRepository.save(newCar);

        log.info("CarServiceImpl. Create car. Car id: {}", savedCar.getId());
        return carMapper.toResponse(savedCar);
    }

    @Override
    @Transactional
    public CarResponse updateCar(Long id, CarRequest carRequest) {
        log.debug("CarServiceImpl. Entering in method updateCar");
        Car existCar = getExistingCarById(id);

        log.debug("CarServiceImpl. Update car from car request. Car request: {}", carRequest.toString());
        carMapper.updateFromRequest(carRequest, existCar);
        Car savedCar = carRepository.save(existCar);

        log.info("CarServiceImpl. Update car. Car id: {}", id);
        return carMapper.toResponse(savedCar);
    }

    @Override
    @Transactional
    public Boolean deleteCarById(Long id) {
        Car existCar = getExistingCarById(id);

        existCar.setDriver(null);
        existCar.setIsEnabled(Boolean.FALSE);
        carRepository.save(existCar);

        log.info("CarServiceImpl. Delete car. Car id: {}", id);
        return Boolean.TRUE;
    }

    private Car getExistingCarById(Long id) {
        log.debug("CarServiceImpl. Attempting to find car. Car id: {}", id);

        return carRepository.findCarByIdAndIsEnabledTrue(id)
            .orElseThrow(() -> {
                log.error("CarServiceImpl. Car not found. car id: {}", id);
                return new CarNotFoundException(
                    ExceptionMessageConstant.CAR_NOT_FOUND_MESSAGE.formatted(id)
                );
            });
    }

}
