package com.example.training;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AppDatabase;
import com.example.madproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TrainingFragment extends Fragment {

    private RecyclerView recyclerView;
    private CourseAdapter adapter;
    private List<Course> courseList;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_training, container, false);

            // RecyclerView setup
            recyclerView = view.findViewById(R.id.recyclerCourses);
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

            // Initialize list and adapter
            courseList = new ArrayList<>();
            adapter = new CourseAdapter(requireContext(), courseList);
            recyclerView.setAdapter(adapter);

            // FAB setup with role-based visibility
            FloatingActionButton fabAddCourse = view.findViewById(R.id.fabAddCourse);
            applyRolePermissions(fabAddCourse);


            // Get database instance
            db = AppDatabase.getDatabase(requireContext());

            // Seed courses if empty and observe database
            seedAndObserveCourses();

            return view;
        } catch (Exception e) {
            Log.e("TrainingFragment", "Error in onCreateView", e);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    /** Seeds sample courses if the database is empty and observes LiveData for updates */
    private void seedAndObserveCourses() {
        if (db == null || db.courseDao() == null) return;

        new Thread(() -> {
            // Synchronously check if database is empty
            List<Course> courses = db.courseDao().getAllCoursesSync();

            if (courses.isEmpty()) {
                // Seed sample courses
                db.courseDao().insertCourse(new Course(
                        "Android Basics",
                        "Mobile Development",
                        "3 hours",
                        "https://developer.android.com/courses/android-basics-compose/course"
                ));
                db.courseDao().insertCourse(new Course(
                        "Java Fundamentals",
                        "Programming",
                        "2.5 hours",
                        "https://www.w3schools.com/java/"
                ));
                db.courseDao().insertCourse(new Course(
                        "Interview Tips",
                        "General",
                        "7 mins",
                        "https://www.indeed.com/career-advice/interviewing/job-interview-tips-how-to-make-a-great-impression"
                ));
            }

            // Observe LiveData on main thread to update RecyclerView
            requireActivity().runOnUiThread(() -> db.courseDao().getAllCourses().observe(
                    getViewLifecycleOwner(),
                    liveCourses -> {
                        courseList.clear();
                        if (liveCourses != null && !liveCourses.isEmpty()) {
                            courseList.addAll(liveCourses);
                        }
                        adapter.notifyDataSetChanged();
                    }
            ));
        }).start();
    }

    /** Shows FAB only for employer users */
    private void applyRolePermissions(FloatingActionButton fabAddCourse) {
        String userType = getUserType();
        boolean isEmployer = "employer".equalsIgnoreCase(userType);

        fabAddCourse.setVisibility(isEmployer ? View.VISIBLE : View.GONE);

        if (isEmployer) {
            fabAddCourse.setOnClickListener(v ->
                    startActivity(new Intent(requireContext(), AddCourseActivity.class))
            );
        } else {
            fabAddCourse.setOnClickListener(null);
        }
    }


    private String getUserType() {
        return requireContext()
                .getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getString("user_type", "employee"); // default to employee
    }
}
