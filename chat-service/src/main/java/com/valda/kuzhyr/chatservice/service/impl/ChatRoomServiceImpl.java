package com.valda.kuzhyr.chatservice.service.impl;

import com.valda.kuzhyr.chatservice.persistence.entity.ChatRoom;
import com.valda.kuzhyr.chatservice.persistence.repository.ChatRoomRepository;
import com.valda.kuzhyr.chatservice.service.ChatRoomService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;


    @Override
    public Optional<String> getChatId(String senderId, String recipientId, boolean createIfNotExist) {

        return chatRoomRepository
            .findBySenderIdAndRecipientId(senderId, recipientId)
            .map(ChatRoom::getChatId)
            .or(() -> {
                if(!createIfNotExist) {
                    return  Optional.empty();
                }
                var chatId =
                    String.format("%s_%s", senderId, recipientId);

                ChatRoom senderRecipient = ChatRoom
                    .builder()
                    .chatId(chatId)
                    .senderId(senderId)
                    .recipientId(recipientId)
                    .build();

                ChatRoom recipientSender = ChatRoom
                    .builder()
                    .chatId(chatId)
                    .senderId(recipientId)
                    .recipientId(senderId)
                    .build();
                chatRoomRepository.save(senderRecipient);
                chatRoomRepository.save(recipientSender);

                return Optional.of(chatId);
            });

    }
}
