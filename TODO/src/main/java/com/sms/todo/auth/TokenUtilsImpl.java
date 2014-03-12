package com.sms.todo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.sms.todo.dao.UserDAO;
import com.sms.todo.model.User;

@Component
public class TokenUtilsImpl implements TokenUtils
{
    @Autowired private TokenService service;
    @Autowired private UserDAO dao;
    
    @Override
    public String createToken(UserDetails userDetails)
    {
        String key = service.allocateToken(userDetails.getUsername()).getKey();
        User user = dao.findUserByName(userDetails.getUsername());
        user.setToken(key);
        dao.updateToken(user);
        return key;
    }

    @Override
    public boolean validate(String token)
    {
        return dao.findUserByToken(token) != null;
    }

    @Override
    public User getUserFromToken(String token)
    {
        return dao.findUserByToken(token);
    }
}
