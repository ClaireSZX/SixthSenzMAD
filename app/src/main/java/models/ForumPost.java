package com.example.sixthsenzM5.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ForumPost {

    private String postID;
    private String authorID;
    private String username;
    private long timestamp;
    private String content;
    private List<String> tags; // optional tags/hashtags

    // Default constructor required for Firebase
    public ForumPost() { }

    public ForumPost(String postID, String authorID, String username, String content, long timestamp, List<String> tags) {
        this.postID = postID;
        this.authorID = authorID;
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
        this.tags = tags;
    }

    public static ForumPost fromMap(String docID, Map<String, Object> data) {
        if (data == null) return null;

        String authorID = data.get("authorID") != null ? data.get("authorID").toString() : "";
        String username = data.get("username") != null ? data.get("username").toString() : "";
        String content = data.get("content") != null ? data.get("content").toString() : "";

        // Handle timestamp safely
        Object tsObj = data.get("timestamp");
        long timestamp = 0;
        if (tsObj instanceof Number) {
            timestamp = ((Number) tsObj).longValue();
        } else if (tsObj instanceof String) {
            try {
                timestamp = Long.parseLong((String) tsObj);
            } catch (NumberFormatException e) {
                timestamp = 0; // fallback
            }
        }

        // Handle tags safely
        List<String> tags = new ArrayList<>();
        Object tagsObj = data.get("tags");
        if (tagsObj instanceof List<?>) {
            for (Object item : (List<?>) tagsObj) {
                if (item != null) tags.add(item.toString());
            }
        } else if (tagsObj instanceof String) {
            tags.add((String) tagsObj);
        }

        ForumPost post = new ForumPost(docID, authorID, username, content, timestamp, tags);
        post.setPostID(docID);
        return post;
    }


    // Getters and Setters
    public String getPostID() { return postID; }
    public void setPostID(String postId) { this.postID = postID; }

    public String getAuthorID() { return authorID; }
    public void setAuthorID(String authorID) { this.authorID = authorID; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
}
