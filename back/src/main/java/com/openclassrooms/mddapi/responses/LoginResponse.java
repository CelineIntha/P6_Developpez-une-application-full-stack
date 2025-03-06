package com.openclassrooms.mddapi.responses;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.Instant;

@JsonPropertyOrder({"status", "message", "token", "expiresIn", "timestamp"})
public class LoginResponse {

    private int status;
    private String message;
    private String token;
    private long expiresIn;
    private Instant timestamp;

    public int getStatus() {
        return status;
    }

    public LoginResponse setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public LoginResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getToken() {
        return token;
    }

    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public LoginResponse setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
