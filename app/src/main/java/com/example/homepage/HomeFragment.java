package com.example.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobsearch.Job;
import com.example.jobsearch.JobSearchActivity;
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

        Button btnGoToSearch = view.findViewById(R.id.btnGoToSearch);

        btnGoToSearch.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), JobSearchActivity.class);
            startActivity(intent);
        });

        return view;
    }


    private void loadJobs() {
        jobList.add(new Job(
                "Software Engineer",
                "Google",
                "Mountain View",
                "Technology",
                "Java, Python, Cloud",
                "$120k–$150k",
                "5 km",
                "92%"
        ));

        jobList.add(new Job(
                "Android Developer",
                "Facebook",
                "Menlo Park",
                "Social Media",
                "Kotlin, Android SDK, Firebase",
                "$110k–$140k",
                "8 km",
                "88%"
        ));

        jobList.add(new Job(
                "Data Scientist",
                "Amazon",
                "Seattle",
                "E-commerce",
                "Python, Machine Learning, SQL",
                "$130k–$160k",
                "12 km",
                "90%"
        ));

        jobList.add(new Job(
                "Product Manager",
                "Spotify",
                "New York",
                "Music Streaming",
                "Roadmap Planning, Agile, Data Analysis",
                "$115k–$145k",
                "6 km",
                "85%"
        ));



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
