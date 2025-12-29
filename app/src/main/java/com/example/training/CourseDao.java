package com.example.training;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CourseDao {

    // Insert a course into the database
    @Insert
    void insertCourse(Course course);

    // Synchronous method for seeding / checking if database is empty
    @Query("SELECT * FROM Course")
    List<Course> getAllCoursesSync();

    // LiveData method for observing the database
    @Query("SELECT * FROM Course")
    LiveData<List<Course>> getAllCourses();
}