package com.vlad.kuzhyr.rideservice.service.imp.unittest;

import com.vlad.kuzhyr.rideservice.constant.UnitTestDataProvider;
import com.vlad.kuzhyr.rideservice.persistence.entity.KafkaMessage;
import com.vlad.kuzhyr.rideservice.persistence.repository.KafkaMessageRepository;
import com.vlad.kuzhyr.rideservice.service.impl.KafkaMessageServiceImpl;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KafkaMessageServiceImplTest {

    @Mock
    private KafkaMessageRepository kafkaMessageRepository;

    @InjectMocks
    private KafkaMessageServiceImpl kafkaMessageService;

    private KafkaMessage kafkaMessage;

    @BeforeEach
    void setUp() {
        kafkaMessage = UnitTestDataProvider.kafkaMessage();
    }

    @Test
    void getUnsentMessages_shouldReturnUnsentMessages() {
        when(kafkaMessageRepository.findByIsSentFalse()).thenReturn(Collections.singletonList(kafkaMessage));

        List<KafkaMessage> result = kafkaMessageService.getUnsentMessages();

        assertEquals(1, result.size());
        assertEquals(kafkaMessage, result.get(0));

        verify(kafkaMessageRepository).findByIsSentFalse();
    }

    @Test
    void saveMessage_shouldSaveMessage() {
        kafkaMessageService.saveMessage("test-topic", 1L, "test-message");

        verify(kafkaMessageRepository).save(any(KafkaMessage.class));
    }

    @Test
    void markAsSent_shouldMarkMessageAsSent() {
        kafkaMessageService.markAsSent(kafkaMessage);

        verify(kafkaMessageRepository).save(kafkaMessage);
        assertTrue(kafkaMessage.getIsSent());
        assertNotNull(kafkaMessage.getSentAt());
    }

    @Test
    void deleteSentMessages_shouldDeleteSentMessages() {
        kafkaMessageService.deleteSentMessages();

        verify(kafkaMessageRepository).deleteByIsSent(true);
    }
}