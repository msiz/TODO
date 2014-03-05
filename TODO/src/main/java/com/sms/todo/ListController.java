package com.sms.todo;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/api")
public class ListController
{
    private static final Logger logger = LoggerFactory.getLogger(ListController.class);

    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Locale locale, Model model)
    {
        return "list";
    }

}
