package com.sms.todo;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/auth")
public class AuthController
{
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> login(@RequestParam(value = "login") String login, 
            @RequestParam(value = "password") String password)
    {
        Map<String, String> response = new HashMap<String, String>();
        response.put("token", "111");
        return response;
    }

}
