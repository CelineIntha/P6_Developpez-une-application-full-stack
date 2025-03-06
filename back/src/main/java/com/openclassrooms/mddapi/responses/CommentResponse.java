package com.openclassrooms.mddapi.responses;

import java.util.Date;

public class CommentResponse {
    private Long id;
    private String content;
    private String author;
    private Date createdAt;

    public CommentResponse(Long id, String content, String author, Date createdAt) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public String getAuthor() { return author; }
    public Date getCreatedAt() { return createdAt; }
}
