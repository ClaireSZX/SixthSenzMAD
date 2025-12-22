package com.example.sixthsenzM5.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment; // Correct Base Class!
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sixthsenzM5.R;
import com.example.sixthsenzM5.adapters.CourseAdapter;
import com.example.sixthsenzM5.models.Course;
import com.example.sixthsenzM5.utils.CourseDataFetcher;

import java.util.ArrayList;
import java.util.List;

public class TrainingFragment extends Fragment { // ⬅️ Must extend Fragment, NOT AppCompatActivity

    private static final String TAG = "TrainingFragment";
    private RecyclerView recyclerView;
    private CourseAdapter adapter;
    private List<Course> courseList;

    /**
     * 1. Inflates the layout for the Fragment (replaces setContentView)
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // ASSUMPTION: Layout file is fragment_training.xml (should be renamed from activity_training.xml)
        return inflater.inflate(R.layout.fragment_training, container, false);
    }

    /**
     * 2. Initializes views and performs post-creation setup (replaces most of onCreate)
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Initialization
        // Must call findViewById on the inflated 'view' object, NOT directly
        recyclerView = view.findViewById(R.id.recycler_view_courses);
        courseList = new ArrayList<>();

        // Ensure context is available (it should be, but it's good practice to check)
        Context context = getContext();
        if (context == null) return;

        // 2. Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        // Use getContext() or the local 'context' variable
        adapter = new CourseAdapter(context, courseList);
        recyclerView.setAdapter(adapter);

        // 3. Fetch Data
        fetchCourses();
    }

    private void fetchCourses() {
        // Ensure context is available before attempting network operations
        Context context = getContext();
        if (context == null) return;

        CourseDataFetcher.fetchCoursesFromFirestore(new CourseDataFetcher.CourseFetchCallback() {
            @Override
            public void onCoursesFetched(List<Course> fetchedList) {
                // Ensure the fragment is still attached to prevent crashes
                if (isAdded()) {
                    courseList.clear();
                    courseList.addAll(fetchedList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Exception e) {
                if (isAdded()) {
                    Log.e(TAG, "Error fetching courses: " + e.getMessage());
                    // Use getActivity() or getContext() for Toast
                    Toast.makeText(getContext(), "Failed to load courses.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}