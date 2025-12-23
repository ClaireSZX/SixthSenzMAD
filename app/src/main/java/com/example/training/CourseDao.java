package com.example.training;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CourseDao {

    @Query("SELECT * FROM Course")
    LiveData<List<Course>> getAllCourses();

    @Insert
    void insertCourse(Course course);

    // optionally: delete, update methods
}
