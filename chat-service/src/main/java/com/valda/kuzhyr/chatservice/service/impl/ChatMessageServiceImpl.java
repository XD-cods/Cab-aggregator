package com.valda.kuzhyr.chatservice.service.impl;

import com.valda.kuzhyr.chatservice.persistence.entity.ChatMessage;
import com.valda.kuzhyr.chatservice.persistence.entity.MessageStatus;
import com.valda.kuzhyr.chatservice.persistence.repository.ChatMessageRepository;
import com.valda.kuzhyr.chatservice.persistence.repository.ChatRoomRepository;
import com.valda.kuzhyr.chatservice.service.ChatMessageService;
import com.valda.kuzhyr.chatservice.service.ChatRoomService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    private final ChatRoomService chatRoomService;

    private MongoOperations mongoOperations;

    @Override
    public ChatMessage sendNewChatMessage(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    @Override
    public long countNewChatMessages(String senderId, String recipientId) {
        return chatMessageRepository.countBySenderIdAndRecipientIdAndStatus(
            senderId, recipientId, MessageStatus.RECEIVED
        );
    }

    @Override
    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getChatId(senderId, recipientId, false);

        List<ChatMessage> messages =
            chatId.map(chatMessageRepository::findByChatId).orElse(new ArrayList<>());

        if(!messages.isEmpty()) {
            updateChatMessageStatuses(senderId, recipientId, MessageStatus.DELIVERED);
        }

        return messages;
    }

    @Override
    public ChatMessage findByChatMessageById(String id) {
        return chatMessageRepository
            .findById(id)
            .map(chatMessage -> {
                chatMessage.setStatus(MessageStatus.DELIVERED);
                return chatMessageRepository.save(chatMessage);
            })
            .orElseThrow(() ->
                new RuntimeException("can't find message (" + id + ")"));
    }

    @Override
    public void updateChatMessageStatuses(String senderId, String recipientId, MessageStatus status) {
        Query query = new Query(
            Criteria
                .where("senderId").is(senderId)
                .and("recipientId").is(recipientId));
        Update update = Update.update("status", status);
        mongoOperations.updateMulti(query, update, ChatMessage.class);
    }

}
