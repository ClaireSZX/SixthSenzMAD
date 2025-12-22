package com.example.training;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "comments")
public class Comment {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String postId;
    private String author;
    private String content;
    private long timestamp;

    public Comment() {} // Room requires no-arg constructor

    public Comment(String postId, String author, String content, long timestamp) {
        this.postId = postId;
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters
    public int getId() { return id; }
    public String getPostId() { return postId; }
    public String getAuthor() { return author; }
    public String getContent() { return content; }
    public long getTimestamp() { return timestamp; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setPostId(String postId) { this.postId = postId; }
    public void setAuthor(String author) { this.author = author; }
    public void setContent(String content) { this.content = content; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
