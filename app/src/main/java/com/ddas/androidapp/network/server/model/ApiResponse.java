package com.ddas.androidapp.network.server.model;

public class ApiResponse<T>
{
    // Getters, setters
    public String getStatus() { return status; }
    public String getPath() { return path; }
    public T getData() { return data; }
    public String getMessage() { return message; }
    public String getTimestamp() { return timestamp; }
    public void setStatus(String status) { this.status = status; }
    public void setPath(String path) { this.path = path; }
    public void setData(T data) { this.data = data; }
    public void setMessage(String message) { this.message = message; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString()
    {
        return "ApiResponse{" +
                "status='" + status + '\'' +
                ", path='" + path + '\'' +
                ", data=" + data +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    private String status;
    private String path;
    private T data;
    private String message;
    private String timestamp;

}
