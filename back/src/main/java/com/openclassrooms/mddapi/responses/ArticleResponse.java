package com.openclassrooms.mddapi.responses;

import java.util.Date;
import java.util.List;

public class ArticleResponse {
    private Long id;
    private String title;
    private String content;
    private Date createdAt;
    private String author;
    private String topic;
    private Long topicId;
    private List<CommentResponse> comments;

    public ArticleResponse(Long id, String title, String content, Date createdAt, String author, String topic, Long topicId, List<CommentResponse> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.author = author;
        this.topic = topic;
        this.topicId = topicId;
        this.comments = comments;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public Date getCreatedAt() { return createdAt; }
    public String getAuthor() { return author; }
    public String getTopic() { return topic; }
    public Long getTopicId() { return topicId; }
    public List<CommentResponse> getComments() { return comments; }
}
