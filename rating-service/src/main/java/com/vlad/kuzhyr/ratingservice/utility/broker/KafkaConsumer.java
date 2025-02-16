package com.vlad.kuzhyr.ratingservice.utility.broker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vlad.kuzhyr.ratingservice.persistence.entity.RideInfo;
import com.vlad.kuzhyr.ratingservice.persistence.repository.RideInfoRepository;
import com.vlad.kuzhyr.ratingservice.web.dto.RideInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final ObjectMapper objectMapper;

    private final RideInfoRepository rideInfoRepository;

    @KafkaListener(
        topics = "${spring.kafka.topic.ride-completed-topic}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    @SneakyThrows
    @Transactional
    public void consumeRideCompleted(String message) {
        RideInfoDto rideInfoDto = objectMapper.readValue(message, RideInfoDto.class);
        RideInfo rideInfo = RideInfo.builder()
            .rideId(rideInfoDto.rideId())
            .driverId(rideInfoDto.driverId())
            .passengerId(rideInfoDto.passengerId())
            .build();

        log.info("Received ride completed event: {}", rideInfo);
        rideInfoRepository.save(rideInfo);
    }

}
