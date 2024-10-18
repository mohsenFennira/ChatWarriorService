package com.mohsenfn.chatwarriorservice.service.impl;

import com.mohsenfn.chatwarriorservice.model.User;
import com.mohsenfn.chatwarriorservice.model.enume.Status;
import com.mohsenfn.chatwarriorservice.repository.UserRepository;
import com.mohsenfn.chatwarriorservice.service.UserIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserService implements UserIService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public void saveUser(User user) {
        User storedUser=userRepository.findByNickName(user.getNickName());
        if(storedUser==null){
            user.setStatus(Status.ONLINE);
            userRepository.save(user);
        }
    }

    @Override
    public void disconnect(User user) {
       var storedUser=userRepository.findById(user.getNickName()).orElse(null);
       if(storedUser!=null){
               storedUser.setStatus(Status.OFFLINE);
               userRepository.save(storedUser);
       }
    }

    @Override
    public List<User> findConnectedUsers() {
        return userRepository.findAllByStatus(Status.ONLINE);
    }
}
