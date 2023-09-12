package com.ddas.androidapp.network.model;

public class User
{
    // Getters, setters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public void setId(Long id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    private Long id;
    private String email;
    private String password;
}
