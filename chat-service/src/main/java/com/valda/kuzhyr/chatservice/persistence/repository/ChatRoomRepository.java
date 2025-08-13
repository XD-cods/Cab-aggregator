package com.valda.kuzhyr.chatservice.persistence.repository;

import com.valda.kuzhyr.chatservice.persistence.entity.ChatRoom;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {

    Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);

}