package com.example.sixthsenzM5.models;

public class Comment {

    private String commentID;
    private String postID;   // link to forum post
    private String authorID;
    private String username;
    private long timestamp;
    private String text;

    // Default constructor required for Firebase
    public Comment() { }

    public Comment(String commentID, String postID, String authorID, String username, String text, long timestamp) {
        this.commentID = commentID;
        this.postID = postID;
        this.authorID = authorID;
        this.username = username;
        this.text = text;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getCommentID() { return commentID; }
    public void setCommentID(String commentID) { this.commentID = commentID; }

    public String getPostID() { return postID; }
    public void setPostID(String postID) { this.postID = postID; }

    public String getAuthorID() { return authorID; }
    public void setAuthorID(String authorID) { this.authorID = authorID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
