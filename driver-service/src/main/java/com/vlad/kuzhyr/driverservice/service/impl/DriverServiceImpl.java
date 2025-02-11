package com.vlad.kuzhyr.driverservice.service.impl;

import com.vlad.kuzhyr.driverservice.exception.CarAlreadyExistException;
import com.vlad.kuzhyr.driverservice.exception.CarNotFoundException;
import com.vlad.kuzhyr.driverservice.persistence.entity.Car;
import com.vlad.kuzhyr.driverservice.persistence.entity.Driver;
import com.vlad.kuzhyr.driverservice.persistence.repository.CarRepository;
import com.vlad.kuzhyr.driverservice.persistence.repository.DriverRepository;
import com.vlad.kuzhyr.driverservice.service.DriverService;
import com.vlad.kuzhyr.driverservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.driverservice.utility.mapper.DriverMapper;
import com.vlad.kuzhyr.driverservice.utility.mapper.PageResponseMapper;
import com.vlad.kuzhyr.driverservice.web.request.DriverRequest;
import com.vlad.kuzhyr.driverservice.web.request.DriverUpdateCarsRequest;
import com.vlad.kuzhyr.driverservice.web.response.DriverResponse;
import com.vlad.kuzhyr.driverservice.web.response.PageResponse;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    private final DriverMapper driverMapper;

    private final CarRepository carRepository;

    private final PageResponseMapper pageResponseMapper;

    @Override
    public DriverResponse getDriverById(Long id) {
        Driver existDriver = driverRepository.findDriverByIdAndIsEnabledTrue(id)
            .orElseThrow(() -> new CarNotFoundException(
                ExceptionMessageConstant.DRIVER_NOT_FOUND_MESSAGE.formatted(id)
            ));

        return driverMapper.toResponse(existDriver);
    }

    @Override
    public PageResponse<DriverResponse> getAllDriver(Integer currentPage, Integer limit) {
        Pageable pageable = PageRequest.of(currentPage, limit);
        Page<Driver> driversPage = driverRepository.findAll(pageable);

        return pageResponseMapper.toPageResponse(
            driversPage,
            currentPage,
            driverMapper::toResponse
        );
    }

    @Override
    @Transactional
    public DriverResponse createDriver(DriverRequest driverRequest) {
        String driverRequestEmail = driverRequest.email();
        String driverRequestPhone = driverRequest.phone();

        if (driverRepository.existsDriverByEmailAndIsEnabledTrue(driverRequestEmail)) {
            throw new CarAlreadyExistException(
                ExceptionMessageConstant.DRIVER_ALREADY_EXISTS_BY_EMAIL_MESSAGE.formatted(driverRequestEmail)
            );
        }

        if (driverRepository.existsDriverByPhoneAndIsEnabledTrue(driverRequestPhone)) {
            throw new CarAlreadyExistException(
                ExceptionMessageConstant.DRIVER_ALREADY_EXISTS_BY_PHONE_MESSAGE.formatted(driverRequestPhone)
            );
        }

        List<Car> cars = carRepository.findAllById(driverRequest.carIds());
        Driver newDriver = driverMapper.toEntity(driverRequest);
        cars.forEach(car -> car.setDriver(newDriver));
        newDriver.setCars(cars);
        Driver savedDriver = driverRepository.save(newDriver);
        return driverMapper.toResponse(savedDriver);
    }

    @Override
    @Transactional
    public DriverResponse updateDriver(Long id, DriverRequest driverRequest) {
        Driver existDriver = driverRepository.findDriverByIdAndIsEnabledTrue(id)
            .orElseThrow(() -> new CarNotFoundException(
                ExceptionMessageConstant.DRIVER_NOT_FOUND_MESSAGE.formatted(id)
            ));

        List<Car> cars = carRepository.findAllById(driverRequest.carIds());
        driverMapper.updateFromRequest(driverRequest, existDriver);
        existDriver.setCars(cars);
        cars.forEach(car -> car.setDriver(existDriver));
        driverRepository.save(existDriver);
        return driverMapper.toResponse(existDriver);
    }

    @Override
    @Transactional
    public DriverResponse updateDriverCarsById(Long id, DriverUpdateCarsRequest driverUpdateCarsRequest) {
        Driver existDriver = driverRepository.findDriverByIdAndIsEnabledTrue(id)
            .orElseThrow(() -> new CarNotFoundException(
                ExceptionMessageConstant.DRIVER_NOT_FOUND_MESSAGE.formatted(id)
            ));

        List<Car> cars = carRepository.findAllById(driverUpdateCarsRequest.carIds());
        existDriver.setCars(cars);
        cars.forEach(car -> car.setDriver(existDriver));
        driverRepository.save(existDriver);
        return driverMapper.toResponse(existDriver);
    }

    @Override
    @Transactional
    public Boolean deleteDriverById(Long id) {
        Driver existDriver = driverRepository.findDriverByIdAndIsEnabledTrue(id)
            .orElseThrow(() -> new CarNotFoundException(
                ExceptionMessageConstant.DRIVER_NOT_FOUND_MESSAGE.formatted(id))
            );

        List<Car> cars = existDriver.getCars();
        cars.forEach(car -> car.setDriver(null));
        existDriver.setCars(null);
        existDriver.setIsEnabled(Boolean.FALSE);
        driverRepository.save(existDriver);
        return Boolean.TRUE;
    }

}
