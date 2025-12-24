package com.example.training;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;

import java.util.ArrayList;
import java.util.List;

public class TrainingFragment extends Fragment {

    private RecyclerView recyclerView;
    private CourseAdapter adapter;
    private List<Course> courseList;
    private Course course;

    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_training, container, false);

        recyclerView = view.findViewById(R.id.recyclerCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        courseList = new ArrayList<>();
        loadSampleCourses();

        adapter = new CourseAdapter(getContext(), courseList);
        recyclerView.setAdapter(adapter);


        return view;
    }

    private void loadSampleCourses() {
        courseList.add(new Course("course1", "Interview Skills", "Career", "10 min", "https://www.youtube.com/watch?v=ZU9x1vFx5lI"));
        courseList.add(new Course("course2", "Workplace Safety", "Safety", "15 min", "https://example.com/safety"));
    }
}
