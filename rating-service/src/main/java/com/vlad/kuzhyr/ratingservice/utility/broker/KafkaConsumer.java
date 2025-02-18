package com.vlad.kuzhyr.ratingservice.utility.broker;

import com.vlad.kuzhyr.ratingservice.persistence.entity.RideInfo;
import com.vlad.kuzhyr.ratingservice.persistence.repository.RideInfoRepository;
import com.vlad.kuzhyr.ratingservice.utility.mapper.JsonMapper;
import com.vlad.kuzhyr.ratingservice.web.dto.RideInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final JsonMapper jsonMapper;

    private final RideInfoRepository rideInfoRepository;

    @KafkaListener(
        topics = "${spring.kafka.topic.ride-completed-topic}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    @Transactional
    public void consumeRideCompleted(String message) {
        RideInfoDto rideInfoDto = jsonMapper.fromJson(message, RideInfoDto.class);

        RideInfo rideInfo = RideInfo.builder()
            .rideId(rideInfoDto.rideId())
            .driverId(rideInfoDto.driverId())
            .passengerId(rideInfoDto.passengerId())
            .build();

        log.info("Received ride completed event: {}", rideInfo);
        rideInfoRepository.save(rideInfo);
    }

}
