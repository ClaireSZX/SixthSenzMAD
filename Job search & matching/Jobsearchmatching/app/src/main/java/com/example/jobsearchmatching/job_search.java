package com.example.jobsearchmatching;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment handling the Job Search and Personalized Recommendations.
 * Supports the requirement for a simple, user-friendly interface[cite: 6].
 */
public class job_search extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public job_search() {
        // Required empty public constructor
    }

    public static job_search newInstance(String param1, String param2) {
        job_search fragment = new job_search();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 1. Inflate the layout for this fragment (fragment_job_search.xml)
        View view = inflater.inflate(R.layout.fragment_job_search, container, false);

        // 2. Initialize the RecyclerView for Personalized Recommendations
        RecyclerView recyclerView = view.findViewById(R.id.rvRecommendedJobs);

        // 3. Set a Horizontal layout for easy swiping (Accessible UI) [cite: 6]
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        // 4. Create sample data for Match Results (Category Tagging: Construction, Domestic, Food Service)
        List<job_details> jobs = new ArrayList<>();
        jobs.add(new job_details("Construction Helper", "Construction", "RM 80/Day", "1.2 km", "95% Match"));
        jobs.add(new job_details("Home Cleaner", "Domestic", "RM 50/Day", "0.5 km", "88% Match"));
        jobs.add(new job_details("Waiter", "Food Service", "RM 60/Day", "2.0 km", "80% Match"));

        // 5. Initialize and set the Adaptor to the RecyclerView
        JobAdaptor adaptor = new JobAdaptor(jobs);
        recyclerView.setAdapter(adaptor);

        return view;
    }
}