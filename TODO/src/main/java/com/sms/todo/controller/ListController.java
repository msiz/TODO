package com.sms.todo.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sms.todo.dao.TaskDAO;
import com.sms.todo.dao.UserDAO;
import com.sms.todo.model.Response;
import com.sms.todo.model.Task;
import com.sms.todo.model.User;

@Controller
@RequestMapping(value = "/api")
public class ListController
{
    @Autowired private UserDAO userDAO;
    @Autowired private TaskDAO taskDAO;
    @Autowired private MessageSource messages;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<Task> list()
    {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDAO.findUserByName(userName);
        return user.getTasks();
    }
    
    @RequestMapping(value = "/list", method = RequestMethod.PUT)
    @ResponseBody
    public Response create(@RequestBody MultiValueMap<String,String> body, Locale locale)
    {
        String name = body.getFirst("task");
        if (name == null || name.isEmpty())
        {
            return Response.error(messages.getMessage("api.task.empty", null, locale));
        }
        else if (name.length() > Task.MAX_LENGTH)
        {
            return Response.error(messages.getMessage("api.task.too.long", null, locale));
        }
        
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDAO.findUserByName(userName);
        
        Task task = new Task();
        task.setTask(name);
        task.setUser(user);
        
        taskDAO.addTask(task);
        return Response.data(messages.getMessage("api.task.created", null, locale), task);
    }
    
    @RequestMapping(value = "/list/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Response edit(@PathVariable(value = "id") int id, 
            @RequestParam(value = "task", required = false) String name,
            @RequestParam(value = "priority", required = false) Integer priority,
            Locale locale)
    {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDAO.findUserByName(userName);
        
        for (Task task: user.getTasks())
        {
            if (task.getId() == id)
            {
                if (name != null)
                {
                    if (name.isEmpty())
                    {
                        return Response.error(messages.getMessage("api.task.empty", null, locale));
                    }
                    else if (name.length() > Task.MAX_LENGTH)
                    {
                        return Response.error(messages.getMessage("api.task.too.long", null, locale));
                    }
                    task.setTask(name);
                }
                else if (priority != null)
                {
                    if (priority < Task.MIN_PRIORITY || priority > Task.MAX_PRIORITY)
                    {
                        return Response.error(messages.getMessage("api.task.wrong.priority", null, locale));
                    }
                    task.setPriority(priority);
                }
                else
                {
                    return Response.error(messages.getMessage("api.task.no.changes", null, locale));
                }
                taskDAO.changeTask(task);
                return Response.success(messages.getMessage("api.task.updated", null, locale));
            }
        }
        
        return Response.error(messages.getMessage("api.task.not.found", null, locale));
    }
    
    @RequestMapping(value = "/list/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Object delete(@PathVariable(value = "id") int id, Locale locale)
    {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDAO.findUserByName(userName);
        
        for (Task task: user.getTasks())
        {
            if (task.getId() == id)
            {
                taskDAO.deleteTask(task);
                return Response.success(messages.getMessage("api.task.removed", null, locale));
            }
        }
        
        return Response.error(messages.getMessage("api.task.not.found", null, locale));
    }
}
