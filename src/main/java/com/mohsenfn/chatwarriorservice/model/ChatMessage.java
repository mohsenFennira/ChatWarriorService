package com.mohsenfn.chatwarriorservice.model;

import com.mohsenfn.chatwarriorservice.model.enume.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class ChatMessage {
    @Id
    private String id;
    private String chatId;
    private String content;
    private String senderId;
    private String recipientId;
    private Date timestamp;
    private MessageType messageType;
    private String type;
    private byte[] imageBytes;
    private byte[] videoBytes;
}
