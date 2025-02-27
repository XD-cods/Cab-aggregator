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
        log.debug("updateDriverByIdAndIsBusy: Entering method. Driver id: {}, isBusy: {}",
            driverId,
            isBusy
        );

        Driver driverToUpdate = getDriverById(driverId);
        driverToUpdate.setIsBusy(isBusy);
        Driver savedDriver = driverRepository.save(driverToUpdate);

        log.info("updateDriverByIdAndIsBusy: Driver updated successfully. Driver id: {}, isBusy: {}",
            savedDriver.getId(),
            savedDriver.getIsBusy()
        );
    }

    public Driver getDriverById(Long driverId) {
        log.debug("getDriverById: Attempting to find driver. Driver id: {}", driverId);

        return driverRepository.findById(driverId)
            .orElseThrow(() -> {
                log.error("getDriverById: Driver not found. Driver id: {}", driverId);
                return new DriverNotFoundException(
                    ExceptionMessageConstant.DRIVER_NOT_FOUND_MESSAGE.formatted(driverId)
                );
            });
    }
}
