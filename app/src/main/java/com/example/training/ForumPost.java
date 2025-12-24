package com.example.training;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "forum_posts")
public class ForumPost {
    @PrimaryKey
    @NonNull private String postId;
    private int courseId;
    private String author;
    private String content;
    private long timestamp;
    private int commentCount;


    public ForumPost(@NonNull String postId, int courseId, String author, String content, long timestamp, int commentCount) {
        this.postId = postId;
        this.courseId = courseId;
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
    public int getCourseId() { return courseId; }
}
