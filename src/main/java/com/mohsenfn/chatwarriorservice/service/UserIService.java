package com.mohsenfn.chatwarriorservice.service;

import com.mohsenfn.chatwarriorservice.model.User;

import java.util.List;

public interface UserIService {
    public void saveUser(User user);
    public void disconnect(User user);
    public List<User> findConnectedUsers();
}
