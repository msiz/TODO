package com.sms.todo.dao;

import com.sms.todo.model.User;

public interface UserDAO
{
    User findUserByToken(String token);
    
    User findUserByName(String name);
    
    void updateToken(User user);
}
