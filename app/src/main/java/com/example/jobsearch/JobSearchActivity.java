package com.example.jobsearch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;

import java.util.ArrayList;
import java.util.List;

public class JobSearchActivity extends AppCompatActivity {

    private JobAdaptor adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_search);

        // Back button
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // SearchView
        SearchView searchView = findViewById(R.id.searchView);

        // RecyclerView setup
        RecyclerView recyclerView = findViewById(R.id.rvRecommendedJobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Job data
        List<Job> jobs = new ArrayList<>();
        jobs.add(new Job("Construction Helper", "Local Contractor", "Kuala Lumpur", "Construction", "Manual labor", "RM 80/Day", "1.2 km", "95%"));
        jobs.add(new Job("Home Cleaner", "Private Client", "Petaling Jaya", "Domestic", "Cleaning", "RM 50/Day", "0.5 km", "88%"));
        jobs.add(new Job("Waiter", "Restaurant", "Subang", "Food Service", "Customer Service", "RM 60/Day", "2.0 km", "80%"));

        // Adapter
        adapter = new JobAdaptor(jobs, job -> {
            Intent intent = new Intent(JobSearchActivity.this, JobDetailActivity.class);
            intent.putExtra(JobDetailActivity.EXTRA_TITLE, job.title);
            intent.putExtra(JobDetailActivity.EXTRA_COMPANY, job.company);
            intent.putExtra(JobDetailActivity.EXTRA_CATEGORY, job.industry);
            intent.putExtra(JobDetailActivity.EXTRA_PAY, job.payRate);
            intent.putExtra(JobDetailActivity.EXTRA_DISTANCE, job.distance);
            intent.putExtra(JobDetailActivity.EXTRA_SCORE, job.matchScore);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        // Search filter
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}



