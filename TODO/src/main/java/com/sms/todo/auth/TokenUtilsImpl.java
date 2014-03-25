package com.sms.todo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.token.TokenService;
import org.springframework.stereotype.Component;

import com.sms.todo.dao.UserDAO;
import com.sms.todo.model.User;

@Component
public class TokenUtilsImpl implements TokenUtils
{
    @Autowired private TokenService service;
    @Autowired private UserDAO dao;
    
    @Override
    public String createToken(String userName)
    {
        String key = service.allocateToken(userName).getKey();
        User user = dao.findUserByName(userName);
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

    @Override
    public void deleteToken(String userName)
    {
        User user = dao.findUserByName(userName);
        user.setToken(null);
        dao.updateToken(user);
    }
}
