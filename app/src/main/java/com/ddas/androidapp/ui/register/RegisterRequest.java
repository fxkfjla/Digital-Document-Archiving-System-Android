package com.ddas.androidapp.ui.register;

public class RegisterRequest
{
    // Getters and setters
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRePassword() { return rePassword; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRePassword(String rePassword) { this.rePassword = rePassword; }

    private String email;
    private String password;
    private String rePassword;
}
