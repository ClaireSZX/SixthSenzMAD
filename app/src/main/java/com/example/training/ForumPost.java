package com.example.training;

public class ForumPost {
    private String postId;
    private String author;
    private String content;
    private long timestamp;
    private int commentCount;

    public ForumPost(String postId, String author, String content, long timestamp, int commentCount) {
        this.postId = postId;
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
        this.commentCount = commentCount;
    }

    public String getPostId() { return postId; }
    public String getAuthor() { return author; }
    public String getContent() { return content; }
    public long getTimestamp() { return timestamp; }
    public int getCommentCount() { return commentCount; }

    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }
}
