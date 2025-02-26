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
        log.debug("getDriverById: Entering method. Driver id: {}", id);

        Driver existingDriver = getDriverOrElseThrow(id);

        log.info("getDriverById: Driver found. Driver id: {}", id);
        return driverMapper.toResponse(existingDriver);
    }

    @Override
    public PageResponse<DriverResponse> getAllDriver(Integer currentPage, Integer limit) {
        log.debug("getAllDriver: Entering method. Current page: {}, limit: {}", currentPage, limit);

        Pageable pageable = PageRequest.of(currentPage, limit);
        Page<Driver> driversPage = driverRepository.findAll(pageable);

        PageResponse<DriverResponse> pageResponse =
            pageResponseMapper.toPageResponse(driversPage, currentPage, driverMapper::toResponse);

        log.info("getAllDriver: Page of drivers retrieved. Current page: {}, total pages: {}," + " total elements: {}",
            pageResponse.currentPage(), pageResponse.totalPages(), pageResponse.totalElements());
        return pageResponse;
    }

    @Override
    @Transactional
    public DriverResponse createDriver(DriverRequest driverRequest) {
        log.debug("createDriver: Entering method. Driver request: {}", driverRequest);

        driverValidator.validateDriver(driverRequest.email(), driverRequest.phone());

        Driver newDriver = driverMapper.toEntity(driverRequest);
        List<Car> cars = carRepository.findAllById(driverRequest.carIds());
        setCarsDriver(newDriver, cars);
        Driver savedDriver = driverRepository.save(newDriver);

        log.info("createDriver: Driver created successfully. Driver id: {}", savedDriver.getId());
        return driverMapper.toResponse(savedDriver);
    }

    @Override
    @Transactional
    public DriverResponse updateDriver(Long id, DriverRequest driverRequest) {
        log.debug("updateDriver: Entering method. Driver id: {}, driver request: {}", id, driverRequest);

        driverValidator.validateDriver(driverRequest.email(), driverRequest.phone());

        Driver existingDriver = getDriverOrElseThrow(id);
        List<Car> cars = carRepository.findAllById(driverRequest.carIds());
        driverMapper.updateFromRequest(driverRequest, existingDriver);
        setCarsDriver(existingDriver, cars);
        Driver savedDriver = driverRepository.save(existingDriver);

        log.info("updateDriver: Driver updated successfully. Driver id: {}", id);
        return driverMapper.toResponse(savedDriver);
    }

    @Override
    @Transactional
    public DriverResponse updateDriverCarsById(Long id, DriverUpdateCarsRequest driverUpdateCarsRequest) {
        log.debug("updateDriverCarsById: Entering method. Driver id: {}, cars id's:{}", id, driverUpdateCarsRequest);

        Driver existingDriver = getDriverOrElseThrow(id);
        List<Car> cars = carRepository.findAllById(driverUpdateCarsRequest.carIds());
        setCarsDriver(existingDriver, cars);
        Driver savedDriver = driverRepository.save(existingDriver);

        log.info("updateDriverCarsById: Driver cars updated successfully. Driver id: {}, cars id's: {}", id,
            driverUpdateCarsRequest);
        return driverMapper.toResponse(savedDriver);
    }

    @Override
    @Transactional
    public Boolean deleteDriverById(Long id) {
        log.debug("deleteDriverById: Entering method. Driver id: {}", id);

        Driver existingDriver = getDriverOrElseThrow(id);
        deleteCarsFromDriver(existingDriver);
        existingDriver.setIsEnabled(Boolean.FALSE);
        driverRepository.save(existingDriver);

        log.info("deleteDriverById: Driver deleted successfully. Driver id: {}", id);
        return Boolean.TRUE;
    }

    private void setCarsDriver(Driver existingDriver, List<Car> cars) {
        log.debug("setCarsDriver: Setting cars for driver. Driver id: {}", existingDriver.getId());
        existingDriver.setCars(cars);
        cars.forEach(car -> car.setDriver(existingDriver));
    }

    private void deleteCarsFromDriver(Driver existingDriver) {
        log.debug("deleteCarsFromDriver: Deleting cars from driver. Driver id: {}", existingDriver.getId());
        List<Car> cars = existingDriver.getCars();
        cars.forEach(car -> car.setDriver(null));
        existingDriver.setCars(null);
    }

    private Driver getDriverOrElseThrow(Long id) {
        log.debug("getDriverOrElseThrow: Attempting to find driver. Driver id: {}", id);

        return driverRepository.findDriverByIdAndIsEnabledTrue(id).orElseThrow(() -> {
            log.error("getDriverOrElseThrow: Driver not found. Driver id: {}", id);
            return new DriverNotFoundException(ExceptionMessageConstant.DRIVER_NOT_FOUND_MESSAGE.formatted(id));
        });
    }
}
