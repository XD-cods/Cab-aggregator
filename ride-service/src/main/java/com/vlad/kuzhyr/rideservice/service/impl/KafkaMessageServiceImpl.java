package com.vlad.kuzhyr.rideservice.service.impl;

import com.vlad.kuzhyr.rideservice.persistence.entity.KafkaMessage;
import com.vlad.kuzhyr.rideservice.persistence.repository.KafkaMessageRepository;
import com.vlad.kuzhyr.rideservice.service.KafkaMessageService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KafkaMessageServiceImpl implements KafkaMessageService {

    private final KafkaMessageRepository kafkaMessageRepository;

    @Transactional(readOnly = true)
    @Override
    public List<KafkaMessage> getUnsentMessages() {
        return kafkaMessageRepository.findByIsSentFalse();
    }

    @Transactional
    @Override
    public void saveMessage(String topic, Long key, String message) {
        KafkaMessage kafkaMessage = KafkaMessage.builder()
            .topic(topic)
            .key(key)
            .message(message)
            .build();

        kafkaMessageRepository.save(kafkaMessage);
    }

    @Transactional
    @Override
    public void markAsSent(KafkaMessage kafkaMessage) {
        kafkaMessage.setIsSent(true);
        kafkaMessage.setSentAt(LocalDateTime.now());
        kafkaMessageRepository.save(kafkaMessage);
    }

    @Transactional
    @Override
    public void deleteSentMessages() {
        kafkaMessageRepository.deleteByIsSent(true);
    }

}