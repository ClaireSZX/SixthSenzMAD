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

import com.example.AppDatabase;
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

    public HomeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        emptyText = view.findViewById(R.id.textEmpty);
        recyclerView = view.findViewById(R.id.recyclerJobs);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ItemRecommendation(jobList);
        recyclerView.setAdapter(adapter);

        // Load jobs from DB
        loadJobsFromDatabase();

        Button btnGoToSearch = view.findViewById(R.id.btnGoToSearch);
        btnGoToSearch.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), JobSearchActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadJobsFromDatabase() {
        AppDatabase db = AppDatabase.getDatabase(getContext());

        new Thread(() -> {
            List<Job> dbJobs = db.jobDao().getAllJobs();

            // Update UI on main thread
            getActivity().runOnUiThread(() -> {
                jobList.clear();
                jobList.addAll(dbJobs);
                adapter.notifyDataSetChanged();

                if (jobList.isEmpty()) {
                    emptyText.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    emptyText.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            });

        }).start();
    }
}

