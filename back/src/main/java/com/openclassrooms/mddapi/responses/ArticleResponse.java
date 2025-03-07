package com.openclassrooms.mddapi.responses;

import java.util.Date;
import java.util.List;

public class ArticleResponse {
    private int status;
    private String message;
    private Long id;
    private String title;
    private String content;
    private Date createdAt;
    private String author;
    private String topic;
    private List<CommentResponse> comments;

    public ArticleResponse(int status, String message, Long id, String title, String content, Date createdAt, String author, String topic, List<CommentResponse> comments) {
        this.status = status;
        this.message = message;
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.author = author;
        this.topic = topic;
        this.comments = comments;
    }

    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public Date getCreatedAt() { return createdAt; }
    public String getAuthor() { return author; }
    public String getTopic() { return topic; }
    public List<CommentResponse> getComments() { return comments; }
}
