package com.openclassrooms.mddapi.dto.subscriptions;

import jakarta.validation.constraints.NotNull;

public class SubscriptionDto {

    @NotNull(message = "L'ID du thème est obligatoire")
    private Long topicId;

    public Long getTopicId() { return topicId; }
    public void setTopicId(Long topicId) { this.topicId = topicId; }
}
