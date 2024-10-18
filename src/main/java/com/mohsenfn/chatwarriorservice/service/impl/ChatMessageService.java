package com.mohsenfn.chatwarriorservice.service.impl;

import com.mohsenfn.chatwarriorservice.model.ChatMessage;
import com.mohsenfn.chatwarriorservice.repository.ChatMessageRepository;
import com.mohsenfn.chatwarriorservice.service.ChatMessageIService;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ChatMessageService implements ChatMessageIService {
    @Autowired
    private ChatMessageRepository chatRepository;
    @Autowired
    private ChatRoomService chatRoomService;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        var chatId = chatRoomService
                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                .orElseThrow();

        chatMessage.setChatId(chatId);

        // Assuming imageBytes is a byte array, and you are checking if the content is an image or video.
        if (chatMessage.getImageBytes() != null) {
            // You might need more sophisticated checks for specific file types
            if (isImage(chatMessage.getImageBytes())) {
                chatMessage.setType("image");
            } else if (isVideo(chatMessage.getImageBytes())) {
                chatMessage.setType("video");
            } else {
                throw new IllegalArgumentException("Unknown file type");
            }
        }

        chatRepository.save(chatMessage);
        return chatMessage;
    }

    private boolean isImage(byte[] fileBytes) {
        String fileType = getFileType(fileBytes);
        return fileType.startsWith("image/");
    }

    private boolean isVideo(byte[] fileBytes) {
        String fileType = getFileType(fileBytes);
        return fileType.startsWith("video/");
    }
    private String getFileType(byte[] fileBytes) {
        Tika tika = new Tika();
        // Detect the MIME type based on the file's content
        return tika.detect(fileBytes);
    }
    @Override
    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
        return chatId.map(chatRepository::findByChatId).orElse(new ArrayList<>());
    }

    @Override
    public ChatMessage updateMessageToAddPub( MultipartFile image) throws IOException {
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setContent("it's image");
        chatMessage.setImageBytes(image.getBytes());
        return chatRepository.save(chatMessage);
    }

    @Override
    public ChatMessage updateMessageToAddVideo(MultipartFile video) throws IOException {
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setContent("it's video");
        chatMessage.setImageBytes(video.getBytes());
        //chatMessage.setTimestamp(Date.from(Instant.now()));
        return chatRepository.save(chatMessage);
    }
}
