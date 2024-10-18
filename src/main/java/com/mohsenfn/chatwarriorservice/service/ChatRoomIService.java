package com.mohsenfn.chatwarriorservice.service;

import java.util.Optional;

public interface ChatRoomIService {
    public Optional<String> getChatRoomId(String senderId,String recipientId, boolean createNewRoomIfNotExists);
}
