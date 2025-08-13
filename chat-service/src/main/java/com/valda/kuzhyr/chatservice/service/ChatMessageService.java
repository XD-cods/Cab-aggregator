package com.valda.kuzhyr.chatservice.service;

import com.valda.kuzhyr.chatservice.persistence.entity.ChatMessage;
import com.valda.kuzhyr.chatservice.persistence.entity.MessageStatus;
import java.util.List;

public interface ChatMessageService {

    public ChatMessage sendNewChatMessage(ChatMessage chatMessage);

    public long countNewChatMessages(String senderId, String recipientId);

    public List<ChatMessage> findChatMessages(String senderId, String recipientId);

    public ChatMessage findByChatMessageById(String id);

    public void updateChatMessageStatuses(String senderId, String recipientId, MessageStatus status);

}
