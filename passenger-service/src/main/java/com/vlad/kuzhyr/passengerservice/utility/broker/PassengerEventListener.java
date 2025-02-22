package com.vlad.kuzhyr.passengerservice.utility.broker;


import com.vlad.kuzhyr.passengerservice.exception.PassengerNotFoundException;
import com.vlad.kuzhyr.passengerservice.persistence.entity.Passenger;
import com.vlad.kuzhyr.passengerservice.persistence.repository.PassengerRepository;
import com.vlad.kuzhyr.passengerservice.utility.constant.ExceptionMessageConstant;
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

    private final PassengerRepository passengerRepository;

    @KafkaListener(
        topics = "${spring.kafka.topic.passenger-busy-topic}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumePassengerBusyTopic(
        @Payload Boolean isBusy,
        @Header(KafkaHeaders.RECEIVED_KEY) Long passengerId
    ) {
        log.info("Passenger event listener. Consume passenger busy topic. Passenger id: {}, passenger is busy: {}",
            passengerId,
            isBusy);
        Passenger passenger = passengerRepository.findById(passengerId)
            .orElseThrow(() -> new PassengerNotFoundException(
                ExceptionMessageConstant.PASSENGER_NOT_FOUND_MESSAGE.formatted(passengerId)
            ));

        passenger.setIsBusy(isBusy);
        passengerRepository.save(passenger);

        log.info("Passenger event listener. Update passenger is busy field. passenger id: {}, isBusy: {}",
            passengerId,
            isBusy);
    }

}
