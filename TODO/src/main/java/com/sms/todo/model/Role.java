package com.sms.todo.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "authorities")
@SuppressWarnings("serial")
public class Role implements GrantedAuthority
{
    private static final String DEFAULT_ROLE = "User";
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @Basic(optional = false)
    private int id;
    
    @Column(name = "username")
    @Basic(optional = false)
    private String username;
    
    @Column(name = "authority")
    @Basic(optional = false)
    private String authority;
    
    public Role()
    {
        this.authority = DEFAULT_ROLE;
    }
    
    public int getId()
    {
        return id;
    }
    
    public String getUsername()
    {
        return username;
    }

    @Override
    public String getAuthority()
    {
        return authority;
    }
}
