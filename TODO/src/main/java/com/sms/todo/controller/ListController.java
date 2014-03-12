package com.sms.todo.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping(value = "/list")
    @ResponseBody
    public List<Task> list(Locale locale)
    {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDAO.findUserByName(userName);
        return user.getTasks();
    }
    
    @RequestMapping(value = "/create")
    @ResponseBody
    public Task create(@RequestParam(value = "task") String name)
    {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDAO.findUserByName(userName);
        
        Task task = new Task();
        task.setTask(name);
        task.setUser(user);
        
        taskDAO.addTask(task);
        
        return task;
    }
    
    @RequestMapping(value = "/edit")
    @ResponseBody
    public Response edit(@RequestParam(value = "id") int id, 
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
                    task.setTask(name);
                }
                if (priority != null)
                {
                    task.setPriority(priority);
                }
                taskDAO.changeTask(task);
                return Response.success(messages.getMessage("api.task.updated", null, locale));
            }
        }
        
        return Response.error(messages.getMessage("api.task.not.found", null, locale));
    }
    
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam(value = "task") int id, Locale locale)
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
