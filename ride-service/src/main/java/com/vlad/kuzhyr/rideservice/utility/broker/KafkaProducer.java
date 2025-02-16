package com.vlad.kuzhyr.rideservice.utility.broker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vlad.kuzhyr.rideservice.web.dto.RideInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic.ride-completed-topic}")
    private String rideCompletedTopic;

    @SneakyThrows
    public void sendRideCompletedMessage(RideInfoDto rideInfo) {
        kafkaTemplate.send(rideCompletedTopic, objectMapper.writeValueAsString(rideInfo));
    }

}
