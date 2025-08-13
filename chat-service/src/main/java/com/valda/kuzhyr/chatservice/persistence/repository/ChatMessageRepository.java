package com.valda.kuzhyr.chatservice.persistence.repository;

import com.valda.kuzhyr.chatservice.persistence.entity.ChatMessage;
import com.valda.kuzhyr.chatservice.persistence.entity.MessageStatus;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    long countBySenderIdAndRecipientIdAndStatus(String senderId, String recipientId, MessageStatus messageStatus);

    List<ChatMessage> findByChatId(String chatId);

}
