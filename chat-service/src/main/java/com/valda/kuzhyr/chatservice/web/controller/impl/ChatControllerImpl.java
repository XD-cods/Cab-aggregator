package com.valda.kuzhyr.chatservice.web.controller.impl;

import com.valda.kuzhyr.chatservice.persistence.entity.ChatNotification;
import com.valda.kuzhyr.chatservice.service.ChatMessageService;
import com.valda.kuzhyr.chatservice.service.ChatRoomService;
import com.valda.kuzhyr.chatservice.web.controller.ChatController;
import com.valda.kuzhyr.chatservice.persistence.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ChatControllerImpl implements ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        var chatId = chatRoomService
            .getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage.setChatId(chatId.get());

        ChatMessage saved = chatMessageService.sendNewChatMessage(chatMessage);
        messagingTemplate.convertAndSendToUser(
            chatMessage.getRecipientId(),"/queue/messages",
            new ChatNotification(
                saved.getId(),
                saved.getSenderId(),
                saved.getSenderName()));
    }

    @GetMapping("/messages/{senderId}/{recipientId}/count")
    public ResponseEntity<Long> countNewMessages(
        @PathVariable String senderId,
        @PathVariable String recipientId) {

        return ResponseEntity
            .ok(chatMessageService.countNewChatMessages(senderId, recipientId));
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<?> findChatMessages ( @PathVariable String senderId,
                                                @PathVariable String recipientId) {
        return ResponseEntity
            .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<?> findMessage ( @PathVariable String id) {
        return ResponseEntity
            .ok(chatMessageService.findByChatMessageById(id));
    }

}
