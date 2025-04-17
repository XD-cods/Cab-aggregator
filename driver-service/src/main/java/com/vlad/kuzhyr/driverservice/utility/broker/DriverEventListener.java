package com.vlad.kuzhyr.driverservice.utility.broker;

import com.vlad.kuzhyr.driverservice.utility.mapper.JsonMapper;
import com.vlad.kuzhyr.driverservice.web.dto.external.payload.DriverCreatePayload;
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
    private final JsonMapper jsonMapper;

    @KafkaListener(
        topics = "${spring.kafka.topic.driver-busy-topic}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeDriverBusyEvent(
        @Payload Boolean isBusy,
        @Header(KafkaHeaders.RECEIVED_KEY) Long driverId
    ) {
        log.info("consumeDriverBusyEvent: Consuming driver busy event. Driver id: {}, isBusy: {}",
            driverId,
            isBusy
        );

        driverProcessor.updateDriverByIdAndIsBusy(driverId, isBusy);
    }

    @KafkaListener(
        topics = "${spring.kafka.topic.driver-create-topic}",
        groupId = "driver-auth-group"
    )
    public void consumeDriverCreateTopic(
        String message
    ) {
        log.info("consumeDriverBusyEvent: Consuming driver create topic.");

        DriverCreatePayload driverCreatePayload = jsonMapper.fromJson(message, DriverCreatePayload.class);

        driverProcessor.createNewDriver(driverCreatePayload);

    }
}
