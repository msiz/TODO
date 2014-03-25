package com.sms.todo.auth;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.sms.todo.model.User;

@Component
public class AuthenticationTokenProcessingFilter extends GenericFilterBean
{
    private static final String TOKEN = "token";
    
    @Autowired TokenUtils tokenUtils;
    @Autowired AuthenticationManager authenticationManager;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = req.getHeader(TOKEN);
        if (token == null)
        {
            token = request.getParameter(TOKEN);
        }
        
        if (token != null && !token.isEmpty())
        {
            if (tokenUtils.validate(token))
            {
                User user = tokenUtils.getUserFromToken(token);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user.getUsername(), user.getPassword());
                authentication.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails((HttpServletRequest) request));
                SecurityContextHolder.getContext().setAuthentication(
                        authenticationManager.authenticate(authentication));
            }
        }

        chain.doFilter(request, response);
    }
    
    public void setTokenUtils(TokenUtils tokenUtils)
    {
        this.tokenUtils = tokenUtils;
    }
    
    public void setAuthenticationManager(AuthenticationManager authenticationManager)
    {
        this.authenticationManager = authenticationManager;
    }
}
