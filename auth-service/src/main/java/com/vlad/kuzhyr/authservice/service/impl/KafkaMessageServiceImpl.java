package com.vlad.kuzhyr.authservice.service.impl;

import com.vlad.kuzhyr.authservice.persistence.entity.KafkaMessage;
import com.vlad.kuzhyr.authservice.persistence.repository.KafkaMessageRepository;
import com.vlad.kuzhyr.authservice.service.KafkaMessageService;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
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
    private final Tracer tracer;

    @Transactional(readOnly = true)
    @Override
    public List<KafkaMessage> getUnsentMessages() {
        log.debug("getUnsentMessages: Entering method.");

        List<KafkaMessage> unsentMessages = kafkaMessageRepository.findByIsSentFalse();

        log.debug("getUnsentMessages: Retrieved unsent messages. Count: {}", unsentMessages.size());
        return unsentMessages;
    }

    @Transactional
    @Override
    public void saveMessage(String topic, Long key, String message) {
        log.debug("saveMessage: Entering method. Topic: {}, key: {}, message: {}", topic, key, message);
        String traceparent = getCurrentTraceparent();

        KafkaMessage kafkaMessage = KafkaMessage.builder()
            .topic(topic)
            .key(key)
            .message(message)
            .traceparent(traceparent)
            .build();

        KafkaMessage savedMessage = kafkaMessageRepository.save(kafkaMessage);

        log.debug("saveMessage: Message saved. {}", savedMessage);
    }

    @Transactional
    @Override
    public void markAsSent(KafkaMessage kafkaMessage) {
        log.debug("markAsSent: Entering method. Message id: {}", kafkaMessage.getId());

        kafkaMessage.setIsSent(true);
        kafkaMessage.setSentAt(LocalDateTime.now());
        kafkaMessageRepository.save(kafkaMessage);

        log.debug("markAsSent: Message marked as sent. Message id: {}", kafkaMessage.getId());
    }

    @Transactional
    @Override
    public void deleteSentMessages() {
        log.debug("deleteSentMessages: Entering method.");

        kafkaMessageRepository.deleteByIsSent(true);

        log.debug("deleteSentMessages: Sent messages deleted.");
    }

    private String getCurrentTraceparent() {
        TraceContext context = tracer.currentTraceContext().context();
        return String.format("00-%s-%s-00", context.traceId(), context.spanId());
    }
}
