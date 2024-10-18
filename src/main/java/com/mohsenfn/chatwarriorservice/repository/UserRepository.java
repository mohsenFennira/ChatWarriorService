package com.mohsenfn.chatwarriorservice.repository;

import com.mohsenfn.chatwarriorservice.model.User;
import com.mohsenfn.chatwarriorservice.model.enume.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository  extends MongoRepository<User, String> {
    List<User> findAllByStatus(Status online);

    User findByNickName(String nickName);
}
