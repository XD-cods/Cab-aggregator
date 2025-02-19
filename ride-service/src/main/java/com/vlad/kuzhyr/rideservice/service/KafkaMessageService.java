package com.vlad.kuzhyr.rideservice.service;

import com.vlad.kuzhyr.rideservice.persistence.entity.KafkaMessage;
import java.util.List;

public interface KafkaMessageService {
    List<KafkaMessage> getUnsentMessages();

    void saveMessage(String topic, Long key, String message);

    void markAsSent(KafkaMessage kafkaMessage);

    void deleteSentMessages();
}
