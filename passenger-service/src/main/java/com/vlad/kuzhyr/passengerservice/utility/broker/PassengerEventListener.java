package com.vlad.kuzhyr.passengerservice.utility.broker;


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

}
