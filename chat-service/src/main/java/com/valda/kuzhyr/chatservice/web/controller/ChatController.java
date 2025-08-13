package com.valda.kuzhyr.chatservice.web.controller;

import com.valda.kuzhyr.chatservice.persistence.entity.ChatMessage;
import org.springframework.messaging.handler.annotation.Payload;

public interface ChatController {

    public void processMessage(@Payload ChatMessage message);
}
