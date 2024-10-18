package com.mohsenfn.chatwarriorservice.model;

import com.mohsenfn.chatwarriorservice.model.enume.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String nickName;
    private String fullName;
    private Status status;

}
