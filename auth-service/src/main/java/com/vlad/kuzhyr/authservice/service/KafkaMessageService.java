package com.vlad.kuzhyr.authservice.service;

import com.vlad.kuzhyr.authservice.persistence.entity.KafkaMessage;
import java.util.List;

public interface KafkaMessageService {

    List<KafkaMessage> getUnsentMessages();

    void saveMessage(String topic, Long key, String message);

    void markAsSent(KafkaMessage kafkaMessage);

    void deleteSentMessages();

}
