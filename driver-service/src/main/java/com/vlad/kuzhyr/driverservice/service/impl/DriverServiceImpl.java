package com.vlad.kuzhyr.driverservice.service.impl;

import com.vlad.kuzhyr.driverservice.exception.DriverNotFoundException;
import com.vlad.kuzhyr.driverservice.persistence.entity.Car;
import com.vlad.kuzhyr.driverservice.persistence.entity.Driver;
import com.vlad.kuzhyr.driverservice.persistence.repository.CarRepository;
import com.vlad.kuzhyr.driverservice.persistence.repository.DriverRepository;
import com.vlad.kuzhyr.driverservice.service.DriverService;
import com.vlad.kuzhyr.driverservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.driverservice.utility.mapper.DriverMapper;
import com.vlad.kuzhyr.driverservice.utility.mapper.PageResponseMapper;
import com.vlad.kuzhyr.driverservice.utility.validator.DriverValidator;
import com.vlad.kuzhyr.driverservice.web.dto.request.DriverRequest;
import com.vlad.kuzhyr.driverservice.web.dto.request.DriverUpdateCarsRequest;
import com.vlad.kuzhyr.driverservice.web.dto.response.DriverResponse;
import com.vlad.kuzhyr.driverservice.web.dto.response.PageResponse;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;
    private final CarRepository carRepository;
    private final PageResponseMapper pageResponseMapper;
    private final DriverValidator driverValidator;

    @Override
    public DriverResponse getDriverById(Long id) {
        Driver existDriver = getDriverOrElseThrow(id);

        log.info("Driver service. Get driver by id. driver id: {}", id);
        return driverMapper.toResponse(existDriver);
    }

    @Override
    public PageResponse<DriverResponse> getAllDriver(Integer currentPage, Integer limit) {

        Pageable pageable = PageRequest.of(currentPage, limit);
        Page<Driver> driversPage = driverRepository.findAll(pageable);

        PageResponse<DriverResponse> pageResponse =
            pageResponseMapper.toPageResponse(driversPage, currentPage, driverMapper::toResponse);

        log.info("Driver service. Fetch all drivers. current page:{}, total pages:{}", pageResponse.currentPage(),
            pageResponse.totalPages());

        return pageResponse;
    }

    @Override
    @Transactional
    public DriverResponse createDriver(DriverRequest driverRequest) {
        String driverRequestEmail = driverRequest.email();
        String driverRequestPhone = driverRequest.phone();

        log.debug("Driver service. Create driver. Email: {}, Phone: {}", driverRequestEmail, driverRequestPhone);

        driverValidator.validateDriver(driverRequestEmail, driverRequestPhone);

        Driver newDriver = driverMapper.toEntity(driverRequest);
        List<Car> cars = carRepository.findAllById(driverRequest.carIds());
        setCarsDriver(newDriver, cars);
        Driver savedDriver = driverRepository.save(newDriver);

        log.info("Driver service. Create new driver. Driver id:{}", savedDriver.getId());
        return driverMapper.toResponse(savedDriver);
    }

    @Override
    @Transactional
    public DriverResponse updateDriver(Long id, DriverRequest driverRequest) {
        String driverRequestEmail = driverRequest.email();
        String driverRequestPhone = driverRequest.phone();

        log.debug("Driver service. Update driver. Email: {}, Phone: {}", driverRequestEmail, driverRequestPhone);

        driverValidator.validateDriver(driverRequestEmail, driverRequestPhone);

        Driver existDriver = getDriverOrElseThrow(id);

        List<Car> cars = carRepository.findAllById(driverRequest.carIds());
        driverMapper.updateFromRequest(driverRequest, existDriver);
        setCarsDriver(existDriver, cars);
        Driver savedDriver = driverRepository.save(existDriver);

        log.info("Driver service. Updated driver. Driver id:{}", savedDriver.getId());
        return driverMapper.toResponse(savedDriver);
    }

    @Override
    @Transactional
    public DriverResponse updateDriverCarsById(Long id, DriverUpdateCarsRequest driverUpdateCarsRequest) {
        Driver existDriver = getDriverOrElseThrow(id);

        log.debug("Driver service. Update driver cars. Driver id: {}", id);

        List<Car> cars = carRepository.findAllById(driverUpdateCarsRequest.carIds());
        setCarsDriver(existDriver, cars);
        Driver savedDriver = driverRepository.save(existDriver);

        log.info("Driver service. Updated drivers cars. Driver id:{}", savedDriver.getId());
        return driverMapper.toResponse(savedDriver);
    }

    @Override
    @Transactional
    public Boolean deleteDriverById(Long id) {
        Driver existDriver = getDriverOrElseThrow(id);

        log.debug("Driver service. Delete driver. Driver id: {}", id);

        deleteCarsFromDriver(existDriver);
        existDriver.setIsEnabled(Boolean.FALSE);
        driverRepository.save(existDriver);

        log.info("Driver service. Driver deleted. Driver id:{}", id);
        return Boolean.TRUE;
    }

    private void setCarsDriver(Driver existDriver, List<Car> cars) {
        log.debug("Driver service. Set cars driver. Driver id:{}", existDriver.getId());
        existDriver.setCars(cars);
        cars.forEach(car -> car.setDriver(existDriver));
    }

    private void deleteCarsFromDriver(Driver existDriver) {
        log.debug("Driver service. Delete cars driver. Driver id:{}", existDriver.getId());
        List<Car> cars = existDriver.getCars();
        cars.forEach(car -> car.setDriver(null));
        existDriver.setCars(null);
    }

    private Driver getDriverOrElseThrow(Long id) {
        log.debug("Driver service. Attempting to find driver. Driver id: {}", id);

        return driverRepository.findDriverByIdAndIsEnabledTrue(id).orElseThrow(() -> {
            log.error("Driver service. Driver not found. Driver id: {}", id);
            return new DriverNotFoundException(ExceptionMessageConstant.DRIVER_NOT_FOUND_MESSAGE.formatted(id));
        });
    }

}
