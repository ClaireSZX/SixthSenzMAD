package com.example.jobsearch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AppDatabase;
import com.example.madproject.R;

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

        // Load jobs from database in background thread
        AppDatabase db = AppDatabase.getDatabase(this);

        new Thread(() -> {
            List<Job> jobList = db.jobDao().getAllJobs();

            runOnUiThread(() -> {
                // Initialize adapter after data is loaded
                adapter = new JobAdaptor(jobList, job -> {
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
            });
        }).start();

        // Search filter
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adapter != null) adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
