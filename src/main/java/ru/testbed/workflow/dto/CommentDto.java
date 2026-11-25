package ru.testbed.workflow.dto;

import java.time.Instant;

/** Сгенерировано из manifest.yaml — не править руками. */
public class CommentDto {

    private String id;
    private String author;
    private String message;
    private Integer attachmentsCount;
    private Instant createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Integer getAttachmentsCount() { return attachmentsCount; }
    public void setAttachmentsCount(Integer attachmentsCount) { this.attachmentsCount = attachmentsCount; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
