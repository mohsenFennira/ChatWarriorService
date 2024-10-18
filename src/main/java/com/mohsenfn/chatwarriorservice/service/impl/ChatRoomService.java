package com.mohsenfn.chatwarriorservice.service.impl;

import com.mohsenfn.chatwarriorservice.model.ChatRoom;
import com.mohsenfn.chatwarriorservice.repository.ChatRoomRepository;
import com.mohsenfn.chatwarriorservice.service.ChatRoomIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatRoomService implements ChatRoomIService {
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Override
    public Optional<String> getChatRoomId(String senderId, String recipientId, boolean createNewRoomIfNotExists) {
        return chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(()->{
                    if (createNewRoomIfNotExists){
                        var chatId=createChat(senderId,recipientId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }

    private String createChat(String senderId, String recipientId) {
        var chatId= String.format("%s_%s",senderId,recipientId);
        ChatRoom sendRecipient= ChatRoom.builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();
        ChatRoom recipientSender= ChatRoom.builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();
           chatRoomRepository.save(sendRecipient);
           chatRoomRepository.save(recipientSender);
        return chatId;
    }

}
