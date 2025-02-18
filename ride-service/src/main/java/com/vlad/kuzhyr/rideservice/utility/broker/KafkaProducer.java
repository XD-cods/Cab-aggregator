package com.vlad.kuzhyr.rideservice.utility.broker;

import com.vlad.kuzhyr.rideservice.service.impl.KafkaMessageService;
import com.vlad.kuzhyr.rideservice.utility.mapper.JsonMapper;
import com.vlad.kuzhyr.rideservice.web.dto.RideInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaMessageService kafkaMessageService;

    @Value("${spring.kafka.topic.ride-completed-topic}")
    private String rideCompletedTopic;

    @Value("${spring.kafka.topic.driver-busy-topic}")
    private String driverBusyTopic;

    private final JsonMapper jsonMapper;

    @Transactional
    public void sendRideCompletedMessage(RideInfoDto rideInfo) {
        String jsonMessage = jsonMapper.toJson(rideInfo);

        kafkaMessageService.saveMessage(rideCompletedTopic, null, jsonMessage);
    }

    @Transactional
    public void sendDriverBusyMessage(Long driverId, Boolean isBusy) {
        String jsonMessage = jsonMapper.toJson(isBusy);

        kafkaMessageService.saveMessage(driverBusyTopic, driverId, jsonMessage);
    }

}
