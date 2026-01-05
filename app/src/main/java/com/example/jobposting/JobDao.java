package com.example.jobposting;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface JobDao {

    @Insert
    void insert(Job job);

    @Query("SELECT * FROM Job")
    List<Job> getAllJobs();
}
