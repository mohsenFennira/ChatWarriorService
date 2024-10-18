package com.mohsenfn.chatwarriorservice.controller;

import com.mohsenfn.chatwarriorservice.model.ChatMessage;
import com.mohsenfn.chatwarriorservice.model.ChatNotification;
import com.mohsenfn.chatwarriorservice.service.ChatMessageIService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@Controller
public class ChatController {
      @Autowired
      private ChatMessageIService  chatMessageIService;

      @Autowired
      private SimpMessagingTemplate messagingTemplate;
     @GetMapping("/messages/{senderId}/{recipientId}")
     public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
                                                               @PathVariable String recipientId) {
          return ResponseEntity
                  .ok(chatMessageIService.findChatMessages(senderId, recipientId));
     }


    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        if (chatMessage.getRecipientId() == null || chatMessage.getRecipientId().isEmpty()) {
            // Log an error or throw an exception
            throw new IllegalArgumentException("Recipient ID cannot be null or empty");
        }

        ChatMessage savedMsg = chatMessageIService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent(),
                        savedMsg.getImageBytes(),
                        savedMsg.getVideoBytes(),
                        savedMsg.getType()
                )
        );
    }
/*
     @PostMapping("/addImage")
     @ResponseBody
     public ChatMessage updateMessageToAddPub(@RequestParam("image") MultipartFile image) throws IOException {
         return chatMessageIService.updateMessageToAddPub(image);
    }
    @PostMapping("/addVideo")
    @ResponseBody
    public ChatMessage updateMessageToAddVideo(@RequestParam("video") MultipartFile video) throws IOException {
        return chatMessageIService.updateMessageToAddVideo(video);
    }

 */
}
