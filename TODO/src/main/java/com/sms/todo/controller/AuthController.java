package com.sms.todo.controller;

import java.util.Arrays;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sms.todo.auth.TokenUtils;
import com.sms.todo.model.Response;
import com.sms.todo.model.Role;

@Controller
@RequestMapping(value = "/auth")
public class AuthController
{
    @Autowired private TokenUtils tokenUtils;
    @Autowired private UserDetailsManager manager;
    @Autowired private MessageSource messages;
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Response login(@RequestParam(value = "login") String login, 
            @RequestParam(value = "password") String password,
            Locale locale)
    {
        if (login.isEmpty())
        {
            return Response.error(messages.getMessage("auth.user.empty", null, locale));
        }
        else if (password.isEmpty())
        {
            return Response.error(messages.getMessage("auth.password.empty", null, locale));
        }
        try
        {
            UserDetails details = manager.loadUserByUsername(login);
            if (password.equals(details.getPassword()))
            {
                String token = tokenUtils.createToken(details.getUsername());
                return Response.data(messages.getMessage("auth.success", null, locale), token);
            }
            else
            {
                return Response.error(messages.getMessage("auth.wrong.password", null, locale));
            }
        }
        catch (UsernameNotFoundException e)
        {
            return Response.error(messages.getMessage("auth.user.not.found", null, locale));
        }
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Response register(@RequestParam(value = "login") String login, 
            @RequestParam(value = "password") String password,
            Locale locale)
    {
        if (login.isEmpty())
        {
            return Response.error(messages.getMessage("auth.user.empty", null, locale));
        }
        else if (password.isEmpty())
        {
            return Response.error(messages.getMessage("auth.password.empty", null, locale));
        }
        else
        {
            if (!manager.userExists(login))
            {
                UserDetails details = new User(login, password, Arrays.asList(new Role()));
                manager.createUser(details);
                String token = tokenUtils.createToken(details.getUsername());
                return Response.data(messages.getMessage("auth.success", null, locale), token);
            }
            else
            {
                return Response.error(messages.getMessage("auth.user.exists", null, locale));
            }
        }
    }
    
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public Response logout(Locale locale)
    {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        tokenUtils.deleteToken(userName);
        SecurityContextHolder.getContext().setAuthentication(null);
        return Response.success(messages.getMessage("auth.logged.out", null, locale));
    }
}
