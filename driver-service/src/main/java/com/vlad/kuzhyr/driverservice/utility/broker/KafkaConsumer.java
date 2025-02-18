package com.vlad.kuzhyr.driverservice.utility.broker;

import com.vlad.kuzhyr.driverservice.exception.DriverNotFoundException;
import com.vlad.kuzhyr.driverservice.persistence.entity.Driver;
import com.vlad.kuzhyr.driverservice.persistence.repository.DriverRepository;
import com.vlad.kuzhyr.driverservice.utility.constant.ExceptionMessageConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final DriverRepository driverRepository;

    @KafkaListener(
        topics = "${spring.kafka.topic.driver-busy-topic}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeDriverBusyEvent(
        @Payload Boolean isBusy,
        @Header(KafkaHeaders.RECEIVED_KEY) Long driverId
    ) {
        Driver driver = driverRepository.findById(driverId)
            .orElseThrow(() -> new DriverNotFoundException(
                ExceptionMessageConstant.DRIVER_NOT_FOUND_MESSAGE.formatted(driverId)
            ));

        driver.setIsBusy(isBusy);
        driverRepository.save(driver);
    }

}
