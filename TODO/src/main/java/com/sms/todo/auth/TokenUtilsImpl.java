package com.sms.todo.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TokenUtilsImpl implements TokenUtils
{

    @Override
    public String getToken(UserDetails userDetails)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getToken(UserDetails userDetails, Long expiration)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean validate(String token)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public UserDetails getUserFromToken(String token)
    {
        // TODO Auto-generated method stub
        return null;
    }

}
