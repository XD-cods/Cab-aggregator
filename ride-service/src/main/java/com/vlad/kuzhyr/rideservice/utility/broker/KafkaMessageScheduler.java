package com.vlad.kuzhyr.rideservice.utility.broker;

import com.vlad.kuzhyr.rideservice.persistence.entity.KafkaMessage;
import com.vlad.kuzhyr.rideservice.service.impl.KafkaMessageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class KafkaMessageScheduler {

    private final KafkaMessageService kafkaMessageService;
    private final KafkaTemplate<Long, Object> kafkaTemplate;

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void processKafkaMessage() {
        List<KafkaMessage> kafkaMessageMessages = kafkaMessageService.getUnsentMessages();

        for (KafkaMessage kafkaMessage : kafkaMessageMessages) {
            if (kafkaMessage.getKey() == null) {
                kafkaTemplate.send(kafkaMessage.getTopic(), kafkaMessage.getMessage());
            } else {
                kafkaTemplate.send(kafkaMessage.getTopic(), kafkaMessage.getKey(), kafkaMessage.getMessage());
            }
            kafkaMessageService.markAsSent(kafkaMessage);
        }
    }
}