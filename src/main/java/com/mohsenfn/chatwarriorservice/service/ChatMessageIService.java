package com.mohsenfn.chatwarriorservice.service;

import com.mohsenfn.chatwarriorservice.model.ChatMessage;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ChatMessageIService {
    public ChatMessage save(ChatMessage chatMessage);
    public List<ChatMessage> findChatMessages(String senderId, String recipientId);

    public ChatMessage updateMessageToAddPub( MultipartFile image) throws IOException;
    public ChatMessage updateMessageToAddVideo( MultipartFile video) throws IOException;


}
