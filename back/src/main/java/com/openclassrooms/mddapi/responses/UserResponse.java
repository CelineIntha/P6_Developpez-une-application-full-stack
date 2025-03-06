package com.openclassrooms.mddapi.responses;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonPropertyOrder({"status", "message", "id", "email", "username", "subscribedTopics"})
public class UserResponse {
    private int status;
    private String message;
    private Long id;
    private String email;
    private String username;
    private List<TopicResponse> subscribedTopics;

    public UserResponse(int status, String message, Long id, String email, String username, List<TopicResponse> subscribedTopics) {
        this.status = status;
        this.message = message;
        this.id = id;
        this.email = email;
        this.username = username;
        this.subscribedTopics = subscribedTopics;
    }

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public List<TopicResponse> getSubscribedTopics() { return subscribedTopics; }

    public UserResponse setStatus(int status) {
        this.status = status;
        return this;
    }

    public UserResponse setMessage(String message) {
        this.message = message;
        return this;
    }
}
