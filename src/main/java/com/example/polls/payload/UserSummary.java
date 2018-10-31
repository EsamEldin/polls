package com.example.polls.payload;

import java.util.Set;

/**
 * @author <egadEldin.ext@orange.com> Essam Eldin
 *
 */
public class UserSummary {
    private Long id;
    private String username;
    private String name;
    private Set<String> roles;

    public UserSummary(Long id, String username, String name,Set<String> roles) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.roles=roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    
    
}