package com.vlad.kuzhyr.rideservice.service.impl;

import com.vlad.kuzhyr.rideservice.persistence.entity.KafkaMessage;
import com.vlad.kuzhyr.rideservice.persistence.repository.KafkaMessageRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KafkaMessageService {

    private final KafkaMessageRepository kafkaMessageRepository;

    @Transactional(readOnly = true)
    public List<KafkaMessage> getUnsentMessages() {
        return kafkaMessageRepository.findByIsSentFalse();
    }

    @Transactional
    public void saveMessage(String topic, Long key, String message) {
        KafkaMessage kafkaMessage = KafkaMessage.builder()
            .topic(topic)
            .key(key)
            .message(message)
            .build();

        kafkaMessageRepository.save(kafkaMessage);
    }

    @Transactional
    public void markAsSent(KafkaMessage kafkaMessage) {
        kafkaMessage.setIsSent(true);
        kafkaMessage.setSentAt(LocalDateTime.now());
        kafkaMessageRepository.save(kafkaMessage);
    }

}