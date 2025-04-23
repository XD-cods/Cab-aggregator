package com.vlad.kuzhyr.passengerservice.utility.broker;


import com.vlad.kuzhyr.passengerservice.utility.mapper.JsonMapper;
import com.vlad.kuzhyr.passengerservice.web.dto.external.PassengerCreatePayload;
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
public class PassengerEventListener {

    private final PassengerProcessor passengerProcessor;
    private final JsonMapper jsonMapper;

    @KafkaListener(
        topics = "${spring.kafka.topic.passenger-busy-topic}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumePassengerBusyTopic(
        @Payload Boolean isBusy,
        @Header(KafkaHeaders.RECEIVED_KEY) Long passengerId
    ) {
        log.info(
            "consumePassengerBusyTopic: Consume passenger busy event. Passenger id: {}, " +
            "isBusy: {}",
            passengerId, isBusy);


        passengerProcessor.updatePassengerByIdAndIsBusy(passengerId, isBusy);
    }

    @KafkaListener(
        topics = "${spring.kafka.topic.passenger-create-topic}",
        groupId = "passenger-auth-group"
    )
    public void consumePassengerCreateTopic(
        String message
    ) {
        log.info("consumePassengerCreateTopic: Consume passenger create event");

        PassengerCreatePayload payload = jsonMapper.fromJson(message, PassengerCreatePayload.class);

        passengerProcessor.createNewPassenger(payload);
    }


}
