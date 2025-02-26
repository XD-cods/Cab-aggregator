package com.vlad.kuzhyr.rideservice.service.impl;

import com.vlad.kuzhyr.rideservice.persistence.entity.KafkaMessage;
import com.vlad.kuzhyr.rideservice.persistence.repository.KafkaMessageRepository;
import com.vlad.kuzhyr.rideservice.service.KafkaMessageService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageServiceImpl implements KafkaMessageService {

    private final KafkaMessageRepository kafkaMessageRepository;

    @Transactional(readOnly = true)
    @Override
    public List<KafkaMessage> getUnsentMessages() {
        log.debug("Kafka message service. Getting unsent messages.");
        List<KafkaMessage> unsentMessages = kafkaMessageRepository.findByIsSentFalse();
        log.info("Kafka message service. Retrieved unsent messages. Unsent messages size: {}", unsentMessages.size());
        return unsentMessages;
    }

    @Transactional
    @Override
    public void saveMessage(String topic, Long key, String message) {
        log.debug("Kafka message service. Saving message. Topic: {}, key: {}", topic, key);
        KafkaMessage kafkaMessage = KafkaMessage.builder()
            .topic(topic)
            .key(key)
            .message(message)
            .build();

        kafkaMessageRepository.save(kafkaMessage);
        log.info("Kafka message service. Saved message. Topic: {}, key: {}", topic, key);
    }

    @Transactional
    @Override
    public void markAsSent(KafkaMessage kafkaMessage) {
        log.debug("Kafka message service. Marking message as sent. Message id: {}", kafkaMessage.getId());
        kafkaMessage.setIsSent(true);
        kafkaMessage.setSentAt(LocalDateTime.now());
        kafkaMessageRepository.save(kafkaMessage);
        log.info("Kafka message service. Marked message as sent. Message id: {}", kafkaMessage.getId());
    }

    @Transactional
    @Override
    public void deleteSentMessages() {
        kafkaMessageRepository.deleteByIsSent(true);
        log.info("Kafka message service. Deleted sent messages.");
    }

}
