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
public class RideEventProducer {
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
        log.debug("sendRideCompletedMessage: Entering method. Ride id: {}", rideInfoPayload.rideId());

        String jsonMessage = jsonMapper.toJson(rideInfoPayload);
        kafkaMessageService.saveMessage(rideCompletedTopic, null, jsonMessage);

        log.info("sendRideCompletedMessage: Sent ride completed event message. Ride id: {}", rideInfoPayload.rideId());
    }

    @Transactional
    public void sendDriverBusyMessage(Long driverId, Boolean isBusy) {
        log.debug("sendDriverBusyMessage: Entering method. Driver id: {}, isBusy: {}", driverId, isBusy);

        String jsonMessage = jsonMapper.toJson(isBusy);
        kafkaMessageService.saveMessage(driverBusyTopic, driverId, jsonMessage);

        log.info("sendDriverBusyMessage: Sent driver busy message. Driver id: {}, isBusy: {}", driverId, isBusy);
    }

    @Transactional
    public void sendPassengerBusyTopic(Long passengerId, Boolean isBusy) {
        log.debug("sendPassengerBusyTopic: Entering method. Passenger id: {}, isBusy: {}", passengerId, isBusy);

        String jsonMessage = jsonMapper.toJson(isBusy);
        kafkaMessageService.saveMessage(passengerBusyTopic, passengerId, jsonMessage);

        log.info("sendPassengerBusyTopic: Sent passenger busy message. Passenger id: {}, isBusy: {}", passengerId,
            isBusy);
    }

}
