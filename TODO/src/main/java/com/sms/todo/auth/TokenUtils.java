package com.sms.todo.auth;

import com.sms.todo.model.User;

public interface TokenUtils
{
    String createToken(String userName);
    void deleteToken(String userName);
    boolean validate(String token);
    User getUserFromToken(String token);
}
