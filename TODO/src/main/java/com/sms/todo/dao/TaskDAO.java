package com.sms.todo.dao;

import com.sms.todo.model.Task;

public interface TaskDAO
{
    void addTask(Task task);
    
    void changeTask(Task task);
    
    void deleteTask(Task task);
}
