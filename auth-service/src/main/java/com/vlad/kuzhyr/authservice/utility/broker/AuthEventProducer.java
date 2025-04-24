package com.vlad.kuzhyr.authservice.utility.broker;

import com.vlad.kuzhyr.authservice.service.KafkaMessageService;
import com.vlad.kuzhyr.authservice.utility.logger.LogUtils;
import com.vlad.kuzhyr.authservice.utility.mapper.JsonMapper;
import com.vlad.kuzhyr.authservice.web.dto.external.payload.DriverCreatePayload;
import com.vlad.kuzhyr.authservice.web.dto.external.payload.PassengerCreatePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class AuthEventProducer {

    private final KafkaMessageService kafkaMessageService;

    @Value("${spring.kafka.topic.driver-create-topic}")
    private String driverCreateTopic;

    @Value("${spring.kafka.topic.passenger-create-topic}")
    private String passengerCreateTopic;

    private final JsonMapper jsonMapper;

    public void sendDriverCreateTopic(DriverCreatePayload driverCreatePayload) {
        String maskedEmail = LogUtils.maskEmail(driverCreatePayload.email());
        log.debug("sendDriverCreateTopic: Entering method. Driver maskedEmail: {}", maskedEmail);

        String jsonMessage = jsonMapper.toJson(driverCreatePayload);
        kafkaMessageService.saveMessage(driverCreateTopic, null, jsonMessage);

        log.info("sendDriverCreateTopic: Sent driver create event message. Driver maskedEmail: {}",
            maskedEmail);
    }

    public void sendPassengerCreateTopic(PassengerCreatePayload passengerCreatePayload) {
        String maskedEmail = LogUtils.maskEmail(passengerCreatePayload.email());
        log.debug("sendPassengerCreateTopic: Entering method. Passenger email: {}", maskedEmail);

        String jsonMessage = jsonMapper.toJson(passengerCreatePayload);
        kafkaMessageService.saveMessage(passengerCreateTopic, null, jsonMessage);

        log.info("sendPassengerCreateTopic: Sent passenger create message. Passenger email: {}", maskedEmail);
    }

}
