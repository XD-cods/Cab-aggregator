package com.vlad.kuzhyr.driverservice.service.impl;

import com.vlad.kuzhyr.driverservice.exception.ResourceAlreadyExistException;
import com.vlad.kuzhyr.driverservice.exception.ResourceNotFoundException;
import com.vlad.kuzhyr.driverservice.persistence.entity.Driver;
import com.vlad.kuzhyr.driverservice.persistence.repository.DriverRepository;
import com.vlad.kuzhyr.driverservice.service.DriverService;
import com.vlad.kuzhyr.driverservice.utility.constant.ExceptionMessageConstant;
import com.vlad.kuzhyr.driverservice.utility.mapper.DriverMapper;
import com.vlad.kuzhyr.driverservice.web.request.DriverRequest;
import com.vlad.kuzhyr.driverservice.web.response.DriverResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
  private final DriverRepository driverRepository;
  private final DriverMapper driverMapper;

  @Override
  public DriverResponse getDriverById(Long id) {
    Driver existDriver = driverRepository.findDriverByIdAndIsEnabledTrue(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                    ExceptionMessageConstant.DRIVER_NOT_FOUND_MESSAGE.formatted(id)
            ));

    return driverMapper.toResponse(existDriver);
  }

  @Override
  public DriverResponse createDriver(DriverRequest driverRequest) {
    validateDriver(driverRequest);

    Driver newDriver = driverMapper.toEntity(driverRequest);
    Driver savedDriver = driverRepository.save(newDriver);
    return driverMapper.toResponse(savedDriver);
  }

  @Override
  public DriverResponse updateDriver(Long id, DriverRequest driverRequest) {
    Driver existDriver = driverRepository.findDriverByIdAndIsEnabledTrue(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                    ExceptionMessageConstant.DRIVER_NOT_FOUND_MESSAGE.formatted(id)
            ));

    driverMapper.updateFromRequest(driverRequest, existDriver);
    driverRepository.save(existDriver);
    return driverMapper.toResponse(existDriver);
  }

  @Override
  public Boolean deleteDriverById(Long id) {
    Driver existDriver = driverRepository.findDriverByIdAndIsEnabledTrue(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                    ExceptionMessageConstant.DRIVER_NOT_FOUND_MESSAGE.formatted(id))
            );

    existDriver.setIsEnabled(Boolean.FALSE);
    driverRepository.save(existDriver);
    return Boolean.TRUE;
  }

  private void validateDriver(DriverRequest driverRequest) {
    String driverRequestEmail = driverRequest.email();
    String driverRequestPhone = driverRequest.phone();
    Long driverCarId = driverRequest.carId();

    if (driverRepository.existsDriverByEmailAndIsEnabledTrue(driverRequestEmail)) {
      throw new ResourceAlreadyExistException(
              ExceptionMessageConstant.DRIVER_ALREADY_EXISTS_BY_EMAIL_MESSAGE.formatted(driverRequestEmail)
      );
    }

    if (driverRepository.existsDriverByPhoneAndIsEnabledTrue(driverRequestPhone)) {
      throw new ResourceAlreadyExistException(
              ExceptionMessageConstant.DRIVER_ALREADY_EXISTS_BY_PHONE_MESSAGE.formatted(driverRequestPhone)
      );
    }
  }

}
