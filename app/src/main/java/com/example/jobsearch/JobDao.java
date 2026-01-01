package com.example.jobsearch;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface JobDao {

    @Insert
    void insert(Job job);

    @Insert
    void insertAll(List<Job> jobs);

    @Query("SELECT * FROM job")
    List<Job> getAllJobs();

    @Query("SELECT COUNT(*) FROM job")
    int getCount();
}
