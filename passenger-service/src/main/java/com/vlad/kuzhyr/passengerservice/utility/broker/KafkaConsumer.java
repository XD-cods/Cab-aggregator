package com.vlad.kuzhyr.passengerservice.utility.broker;


import com.vlad.kuzhyr.passengerservice.exception.PassengerNotFoundException;
import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import com.vlad.kuzhyr.passengerservice.persistence.repository.PassengerRepository;
import com.vlad.kuzhyr.passengerservice.utility.constant.ExceptionMessageConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final PassengerRepository passengerRepository;

    @KafkaListener(
        topics = "${spring.kafka.topic.passenger-busy-topic}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumePassengerBusyTopic(
        @Payload Boolean isBusy,
        @Header(KafkaHeaders.RECEIVED_KEY) Long passengerId
    ) {
        Passenger passenger = passengerRepository.findById(passengerId)
            .orElseThrow(() -> new PassengerNotFoundException(
                ExceptionMessageConstant.PASSENGER_NOT_FOUND_MESSAGE.formatted(passengerId)
            ));

        passenger.setIsBusy(isBusy);
        passengerRepository.save(passenger);
    }

}
