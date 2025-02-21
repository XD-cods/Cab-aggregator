package com.vlad.kuzhyr.driverservice.utility.broker;

import com.vlad.kuzhyr.driverservice.exception.DriverNotFoundException;
import com.vlad.kuzhyr.driverservice.persistence.entity.Driver;
import com.vlad.kuzhyr.driverservice.persistence.repository.DriverRepository;
import com.vlad.kuzhyr.driverservice.utility.constant.ExceptionMessageConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DriverEventListener {

    private final DriverRepository driverRepository;

    @KafkaListener(
        topics = "${spring.kafka.topic.driver-busy-topic}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeDriverBusyEvent(
        @Payload Boolean isBusy,
        @Header(KafkaHeaders.RECEIVED_KEY) Long driverId
    ) {
        log.info("Driver event listener. Consume driver busy topic. Driver id: {}, driver is busy: {}", driverId,
            isBusy);
        Driver driver = driverRepository.findById(driverId)
            .orElseThrow(() -> new DriverNotFoundException(
                ExceptionMessageConstant.DRIVER_NOT_FOUND_MESSAGE.formatted(driverId)
            ));

        driver.setIsBusy(isBusy);
        driverRepository.save(driver);

        log.info("Driver event listener. Update driver is busy field. driver id: {}, isBusy: {}", driverId, isBusy);
    }

}
