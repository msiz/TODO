package com.sms.todo.auth;

import java.io.IOException;
import java.util.Map;

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
    @Autowired TokenUtils tokenUtils;
    @Autowired AuthenticationManager authenticationManager;

    @Override
    @SuppressWarnings("unchecked")
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException
    {
        Map<String, String[]> parms = request.getParameterMap();

        if (parms.containsKey("token"))
        {
            String token = parms.get("token")[0];

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
