package com.sms.todo.model;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User
{
    public static final int MAX_USERNAME_LENGTH = 20;
    public static final int MAX_PASSWORD_LENGTH = 20;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @Basic(optional = false)
    private int id;
    
    @Column(name = "username")
    @Basic(optional = false)
    private String username;
    
    @Column(name = "password")
    @Basic(optional = false)
    private String password;
    
    @Column(name = "enabled")
    @Basic(optional = false)
    private boolean enabled;
    
    @Column(name = "token", length = 1024)
    @Basic(optional = true)
    private String token;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Task> tasks;
    
    public int getId()
    {
        return id;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public String getToken()
    {
        return token;
    }
    
    public boolean isEnabled()
    {
        return enabled;
    }
    
    public List<Task> getTasks()
    {
        return tasks;
    }
    
    public void setToken(String token)
    {
        this.token = token;
    }
}
