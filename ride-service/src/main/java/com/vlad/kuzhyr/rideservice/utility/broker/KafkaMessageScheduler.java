package com.vlad.kuzhyr.rideservice.utility.broker;

import com.vlad.kuzhyr.rideservice.persistence.entity.KafkaMessage;
import com.vlad.kuzhyr.rideservice.service.KafkaMessageService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageScheduler {

    private final KafkaMessageService kafkaMessageService;
    private final KafkaTemplate<Long, Object> kafkaTemplate;

    @Scheduled(fixedRate = 30000)
    @SchedulerLock(
        name = "KafkaMessageScheduler_processKafkaMessage",
        lockAtMostFor = "PT5S",
        lockAtLeastFor = "PT1S"
    )
    public void processKafkaMessage() {
        log.debug("Kafka message scheduler. Processing Kafka messages.");
        List<KafkaMessage> unsentMessages = kafkaMessageService.getUnsentMessages();
        Set<KafkaMessage> uniqueMessages = new HashSet<>(unsentMessages);

        for (KafkaMessage kafkaMessage : uniqueMessages) {
            if (kafkaMessage.getKey() == null) {
                kafkaTemplate.send(kafkaMessage.getTopic(), kafkaMessage.getMessage());
            } else {
                kafkaTemplate.send(kafkaMessage.getTopic(), kafkaMessage.getKey(), kafkaMessage.getMessage());
            }
            this.kafkaMessageService.markAsSent(kafkaMessage);
            log.info("Kafka message scheduler. Sent message. Topic: {}, Key: {}", kafkaMessage.getTopic(),
                kafkaMessage.getKey());
        }
        log.info("Kafka message scheduler. Processed {} Kafka messages.", uniqueMessages.size());
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @SchedulerLock(
        name = "KafkaMessageScheduler_cleanupSentMessages",
        lockAtMostFor = "PT10M",
        lockAtLeastFor = "PT1M"
    )
    public void cleanupSentMessages() {
        log.debug("Kafka message scheduler. Cleaning up sent messages.");
        kafkaMessageService.deleteSentMessages();
        log.info("Kafka message scheduler. Cleaned up sent messages.");
    }
}