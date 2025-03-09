package com.openclassrooms.mddapi.responses;

public class SubscriptionResponse {
    private Long id;
    private Long topicId;
    private String topicName;

    public SubscriptionResponse(Long id, Long topicId, String topicName) {
        this.id = id;
        this.topicId = topicId;
        this.topicName = topicName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTopicId() { return topicId; }
    public void setTopicId(Long topicId) { this.topicId = topicId; }

    public String getTopicName() { return topicName; }
    public void setTopicName(String topicName) { this.topicName = topicName; }
}
