package com.sms.todo.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sms.todo.dao.UserDAO;
import com.sms.todo.model.User;

@Repository
@Transactional
public class UserDAOJPA implements UserDAO
{
    @PersistenceContext
    private EntityManager em;

    @Override
    public User findUserByToken(String token)
    {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE token = :token", User.class);
        query.setParameter("token", token);
        List<User> users = query.getResultList();
        return users.isEmpty() ? null : users.get(0);
    }
    
    @Override
    public User findUserByName(String name)
    {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE username = :name", User.class);
        query.setParameter("name", name);
        List<User> users = query.getResultList();
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public void updateToken(User user)
    {
        Query query = em.createQuery("UPDATE User u SET u.token = :token WHERE u.id = :id");
        query.setParameter("token", user.getToken());
        query.setParameter("id", user.getId());
        query.executeUpdate();
    }
}
