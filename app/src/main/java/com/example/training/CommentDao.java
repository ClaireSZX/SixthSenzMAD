package com.example.training;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CommentDao {

    @Insert
    void insert(Comment comment);

    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY timestamp ASC")
    LiveData<List<Comment>> getCommentsForPost(String postId);

    @Query("SELECT COUNT(*) FROM comments WHERE postId = :postId")
    int countCommentsForPost(String postId);

    @Query("SELECT COUNT(*) FROM comments WHERE postId = :postId")
    LiveData<Integer> getCommentCount(String postId);
}
