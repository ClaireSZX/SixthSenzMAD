package com.example.homepage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.Job;
import com.example.madproject.ItemRecommendation;
import com.example.madproject.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView emptyText;
    private ItemRecommendation adapter;
    private List<Job> jobList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Separate references
        TextView headerTextView = view.findViewById(R.id.tv_recommendation_header);
        emptyText = view.findViewById(R.id.textEmpty);
        recyclerView = view.findViewById(R.id.recyclerJobs);

        // Horizontal layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Adapter
        adapter = new ItemRecommendation(jobList);
        recyclerView.setAdapter(adapter);

        loadJobs(); // Populate jobList

        return view;
    }


    private void loadJobs() {
        // Example data - replace with real recommendations
        jobList.add(new Job(
                "Software Engineer",
                "Google",
                "Mountain View",
                "Java, Python, Cloud",
                "Technology"));

        jobList.add(new Job(
                "Android Developer",
                "Facebook",
                "Menlo Park",
                "Kotlin, Android SDK, Firebase",
                "Social Media"));

        jobList.add(new Job(
                "Data Scientist",
                "Amazon",
                "Seattle",
                "Python, Machine Learning, SQL",
                "E-commerce"));

        jobList.add(new Job(
                "UI/UX Designer",
                "Airbnb",
                "San Francisco",
                "Figma, Adobe XD, User Research",
                "Hospitality"));

        jobList.add(new Job(
                "Backend Developer",
                "Netflix",
                "Los Gatos",
                "Java, Spring Boot, Microservices",
                "Streaming Media"));

        jobList.add(new Job(
                "Cybersecurity Analyst",
                "Microsoft",
                "Redmond",
                "Network Security, Python, Threat Analysis",
                "Technology"));

        jobList.add(new Job(
                "Product Manager",
                "Spotify",
                "New York",
                "Roadmap Planning, Agile, Data Analysis",
                "Music Streaming"));


        // Notify adapter
        adapter.notifyDataSetChanged();

        // Show or hide empty text
        if (jobList.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
