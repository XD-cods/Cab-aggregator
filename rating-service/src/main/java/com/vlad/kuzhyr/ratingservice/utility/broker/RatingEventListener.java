package com.vlad.kuzhyr.ratingservice.utility.broker;

import com.vlad.kuzhyr.ratingservice.persistence.entity.RideInfo;
import com.vlad.kuzhyr.ratingservice.service.RideInfoService;
import com.vlad.kuzhyr.ratingservice.utility.mapper.JsonMapper;
import com.vlad.kuzhyr.ratingservice.utility.mapper.RideInfoMapper;
import com.vlad.kuzhyr.ratingservice.web.dto.external.RideInfoPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RatingEventListener {

    private final JsonMapper jsonMapper;

    private final RideInfoMapper rideInfoMapper;

    private final RideInfoService rideInfoService;

    @KafkaListener(
        topics = "${spring.kafka.topic.ride-completed-topic}",
        groupId = "${spring.kafka.consumer.group-id}"
    )
    @Transactional
    public void consumeRideCompleted(String message) {
        log.info("Rating event listener. Consume ride complete topic message. Message: {}", message);

        RideInfoPayload rideInfoPayload = jsonMapper.fromJson(message, RideInfoPayload.class);

        RideInfo rideInfo = rideInfoMapper.toEntity(rideInfoPayload);

        rideInfoService.saveRideInfo(rideInfo);
    }

}
