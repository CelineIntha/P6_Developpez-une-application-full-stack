package com.openclassrooms.mddapi.responses;

import java.time.LocalDateTime;

public class UpdateUserResponse {

    private int status;
    private String message;
    private String token;
    private LocalDateTime timestamp;

    public UpdateUserResponse(int status, String message, String token) {
        this.status = status;
        this.message = message;
        this.token = token;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public String getToken() { return token; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setStatus(int status) { this.status = status; }
    public void setMessage(String message) { this.message = message; }
    public void setToken(String token) { this.token = token; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
