package com.vlad.kuzhyr.ratingservice.utility.broker;

import com.vlad.kuzhyr.ratingservice.persistence.entity.RideInfo;
import com.vlad.kuzhyr.ratingservice.persistence.repository.RideInfoRepository;
import com.vlad.kuzhyr.ratingservice.utility.mapper.JsonMapper;
import com.vlad.kuzhyr.ratingservice.utility.mapper.RideInfoMapper;
import com.vlad.kuzhyr.ratingservice.web.dto.external.RideInfoPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {

    private final JsonMapper jsonMapper;

    private final RideInfoMapper rideInfoMapper;

    private final RideInfoRepository rideInfoRepository;

    @KafkaListener(
        topics = "${spring.kafka.topic.ride-completed-topic}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    @Transactional
    public void consumeRideCompleted(String message) {
        RideInfoPayload rideInfoPayload = jsonMapper.fromJson(message, RideInfoPayload.class);

        RideInfo rideInfo = rideInfoMapper.toEntity(rideInfoPayload);

        rideInfoRepository.save(rideInfo);
    }

}
