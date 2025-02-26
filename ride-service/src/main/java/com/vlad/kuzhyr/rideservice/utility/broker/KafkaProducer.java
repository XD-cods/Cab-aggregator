package com.vlad.kuzhyr.rideservice.utility.broker;

import com.vlad.kuzhyr.rideservice.service.KafkaMessageService;
import com.vlad.kuzhyr.rideservice.utility.mapper.JsonMapper;
import com.vlad.kuzhyr.rideservice.web.dto.external.RideInfoPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaMessageService kafkaMessageService;

    @Value("${spring.kafka.topic.ride-completed-topic}")
    private String rideCompletedTopic;

    @Value("${spring.kafka.topic.driver-busy-topic}")
    private String driverBusyTopic;

    @Value("${spring.kafka.topic.passenger-busy-topic}")
    private String passengerBusyTopic;

    private final JsonMapper jsonMapper;

    @Transactional
    public void sendRideCompletedMessage(RideInfoPayload rideInfoPayload) {
        log.debug("Kafka producer. Sending ride completed message. Ride id: {}", rideInfoPayload.rideId());
        String jsonMessage = jsonMapper.toJson(rideInfoPayload);

        kafkaMessageService.saveMessage(rideCompletedTopic, null, jsonMessage);
        log.info("Kafka producer. Sent ride completed message. Ride id: {}", rideInfoPayload.rideId());
    }

    @Transactional
    public void sendDriverBusyMessage(Long driverId, Boolean isBusy) {
        log.debug("Kafka producer. Sending driver busy message. Driver id: {}, isBusy: {}", driverId, isBusy);
        String jsonMessage = jsonMapper.toJson(isBusy);

        kafkaMessageService.saveMessage(driverBusyTopic, driverId, jsonMessage);
        log.info("Kafka producer. Sent driver busy message. Driver id: {}, isBusy: {}", driverId, isBusy);
    }

    @Transactional
    public void sendPassengerBusyTopic(Long passengerId, Boolean isBusy) {
        log.debug("Kafka producer. Sending passenger busy message. Passenger id: {}, isBusy: {}", passengerId, isBusy);
        String jsonMessage = jsonMapper.toJson(isBusy);

        kafkaMessageService.saveMessage(passengerBusyTopic, passengerId, jsonMessage);
        log.info("Kafka producer. Sent passenger busy message. Passenger id: {}, isBusy: {}", passengerId, isBusy);
    }

}
