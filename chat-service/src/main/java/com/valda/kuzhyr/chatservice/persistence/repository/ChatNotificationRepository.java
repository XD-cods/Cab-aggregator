package com.valda.kuzhyr.chatservice.persistence.repository;

import com.valda.kuzhyr.chatservice.persistence.entity.ChatNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatNotificationRepository extends MongoRepository<ChatNotification, String> {
}