package com.openclassrooms.mddapi.responses;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

@JsonPropertyOrder({"id", "email", "username", "subscribedTopics"})
public class UserResponse {
    private Long id;
    private String email;
    private String username;
    private List<TopicResponse> subscribedTopics;

    public UserResponse(Long id, String email, String username, List<TopicResponse> subscribedTopics) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.subscribedTopics = subscribedTopics;
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public List<TopicResponse> getSubscribedTopics() { return subscribedTopics; }
}
