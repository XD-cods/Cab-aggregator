package com.vlad.kuzhyr.driverservice.utility.broker;

import com.vlad.kuzhyr.driverservice.exception.DriverNotFoundException;
import com.vlad.kuzhyr.driverservice.persistence.entity.Driver;
import com.vlad.kuzhyr.driverservice.persistence.repository.DriverRepository;
import com.vlad.kuzhyr.driverservice.utility.constant.ExceptionMessageConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DriverProcessor {

    private final DriverRepository driverRepository;

    public void updateDriverByIdAndIsBusy(Long driverId, boolean isBusy) {
        log.debug("Driver processor. Attempting to update driver. Driver id: {}, driver is busy: {}",
            driverId,
            isBusy
        );

        Driver driverToUpdate = getDriverById(driverId);
        driverToUpdate.setIsBusy(isBusy);
        Driver savedDriver = driverRepository.save(driverToUpdate);
        log.debug("Driver processor. Driver updated. Driver id: {}, driver is busy: {}",
            savedDriver.getId(),
            savedDriver.getIsBusy()
        );
    }

    public Driver getDriverById(Long driverId) {
        log.debug("Driver processor. Attempting to get driver. Driver id: {}", driverId);
        return driverRepository.findById(driverId)
            .orElseThrow(() -> {
                log.error("Driver processor. Driver not found. Driver id: {}", driverId);
                return new DriverNotFoundException(
                    ExceptionMessageConstant.DRIVER_NOT_FOUND_MESSAGE.formatted(driverId)
                );
            });
    }
}
