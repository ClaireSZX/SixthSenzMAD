package com.example.training;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ForumPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ForumPost post);

    // Get all posts for a specific course, newest first
    @Query("SELECT * FROM forum_posts WHERE courseId = :courseId COLLATE NOCASE ORDER BY timestamp DESC")
    LiveData<List<ForumPost>> getPostsForCourse(int courseId);

    @Query("SELECT COUNT(*) FROM forum_posts WHERE courseId = :courseId")
    int countPostsForCourse(int courseId);

    // Find a single post by its postId
    @Query("SELECT * FROM forum_posts WHERE postId = :postId LIMIT 1")
    ForumPost findPostById(String postId);

    // Update an existing post (e.g., increment comment count)
    @Update
    void update(ForumPost post);
}
