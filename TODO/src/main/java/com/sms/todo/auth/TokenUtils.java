package com.sms.todo.auth;

import org.springframework.security.core.userdetails.UserDetails;

import com.sms.todo.model.User;

public interface TokenUtils
{
    String createToken(UserDetails userDetails);
    boolean validate(String token);
    User getUserFromToken(String token);
}
