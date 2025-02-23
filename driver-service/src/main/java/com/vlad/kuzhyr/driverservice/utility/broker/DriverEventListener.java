package com.vlad.kuzhyr.driverservice.utility.broker;

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

    private final DriverProcessor driverProcessor;

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

        driverProcessor.updateDriverByIdAndIsBusy(driverId, isBusy);
    }

}
