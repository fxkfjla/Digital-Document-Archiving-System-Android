package com.ddas.androidapp.network.server.model;

public class RetrofitErrorBody
{
    // Getters, setters
    public String getStatus() { return status; }
    public String getPath() { return path; }
    public String getMessage() { return message; }
    public void setStatus(String status) { this.status = status; }
    public void setPath(String path) { this.path = path; }
    public void setMessage(String message) { this.message = message; }

    private String status;
    private String path;
    private String message;
}
