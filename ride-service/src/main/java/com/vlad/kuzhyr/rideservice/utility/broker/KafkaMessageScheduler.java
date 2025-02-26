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

        List<KafkaMessage> unsentMessages = kafkaMessageService.getUnsentMessages();
        Set<KafkaMessage> uniqueMessages = new HashSet<>(unsentMessages);

        log.debug("processKafkaMessage: Found unsent messages. Messages amount: {}", uniqueMessages.size());

        for (KafkaMessage kafkaMessage : uniqueMessages) {
            if (kafkaMessage.getKey() == null) {
                kafkaTemplate.send(kafkaMessage.getTopic(), kafkaMessage.getMessage());
            } else {
                kafkaTemplate.send(kafkaMessage.getTopic(), kafkaMessage.getKey(), kafkaMessage.getMessage());
            }
            kafkaMessageService.markAsSent(kafkaMessage);
            log.debug("processKafkaMessage: Sent message. Topic: {}, Key: {}",
                kafkaMessage.getTopic(), kafkaMessage.getKey());
        }

        log.debug("processKafkaMessage: Processed Kafka messages. Messages amount: {}", uniqueMessages.size());
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @SchedulerLock(
        name = "KafkaMessageScheduler_cleanupSentMessages",
        lockAtMostFor = "PT10M",
        lockAtLeastFor = "PT1M"
    )
    public void cleanupSentMessages() {
        kafkaMessageService.deleteSentMessages();

        log.debug("cleanupSentMessages: Cleaned up sent messages.");
    }
}
