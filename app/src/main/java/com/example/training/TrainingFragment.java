package com.example.training;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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

        View view = inflater.inflate(R.layout.fragment_training, container, false);

        recyclerView = view.findViewById(R.id.recyclerCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        FloatingActionButton fabAddCourse = view.findViewById(R.id.fabAddCourse);

        courseList = new ArrayList<>();
        adapter = new CourseAdapter(requireContext(), courseList);
        recyclerView.setAdapter(adapter);

        db = AppDatabase.getDatabase(requireContext());

        observeCourses();
        fetchCurrentUsername();
        applyRolePermissions(fabAddCourse);


        applyRolePermissions(fabAddCourse);

        return view;
    }

    private void observeCourses() {
        db.CourseDao().getAllCourses().observe(getViewLifecycleOwner(), new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                courseList.clear();
                courseList.addAll(courses);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void applyRolePermissions(FloatingActionButton fabAddCourse) {
        String userType = getUserType();
        boolean isEmployer = "employer".equalsIgnoreCase(userType);

        fabAddCourse.setVisibility(isEmployer ? View.VISIBLE : View.GONE);

        if (isEmployer) {
            fabAddCourse.setOnClickListener(v -> {
                startActivity(new Intent(requireContext(), AddCourseActivity.class));
            });
        } else {
            fabAddCourse.setOnClickListener(null);
        }
    }

    private String getUserType() {
        return requireContext()
                .getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getString("user_type", "employee");
    }

    private void fetchCurrentUsername() {
        String currentUserId = requireContext()
                .getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getString("user_id", null);

        if (currentUserId != null) {
            db.userDao().getUserByEmail(currentUserId).observe(getViewLifecycleOwner(), user -> {
                if (user != null) {
                    String username = user.getName(); // or user.getUsername()
                    // You can now use username, e.g., pass to AddCourseActivity or ForumActivity
                }
            });
        }
    }
}
