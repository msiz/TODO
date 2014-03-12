package com.sms.todo.dao.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sms.todo.dao.TaskDAO;
import com.sms.todo.model.Task;

@Repository
@Transactional
public class TaskDAOJPA implements TaskDAO
{
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public void addTask(Task task)
    {
        em.persist(task);
    }

    @Override
    public void changeTask(Task task)
    {
        em.merge(task);
    }

    @Override
    public void deleteTask(Task task)
    {
        em.remove(em.merge(task));
    }
}
