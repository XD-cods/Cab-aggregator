package com.valda.kuzhyr.chatservice.service;

import com.valda.kuzhyr.chatservice.persistence.entity.ChatRoom;
import java.util.Optional;

public interface ChatRoomService {

    public Optional<String> getChatId(String senderId, String recipientId, boolean createIfNotExist);

}
