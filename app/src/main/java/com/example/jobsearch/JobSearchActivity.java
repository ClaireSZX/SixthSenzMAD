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

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        SearchView searchView = findViewById(R.id.searchView);
        RecyclerView recyclerView = findViewById(R.id.rvRecommendedJobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase db = AppDatabase.getDatabase(this);

        new Thread(() -> {
            List<Job> jobList = db.jobDao().getAllJobs();

            runOnUiThread(() -> {
                // Pass only jobId to JobDetailActivity
                adapter = new JobAdaptor(jobList, job -> {
                    Intent intent = new Intent(this, JobDetailActivity.class);
                    intent.putExtra(JobDetailActivity.EXTRA_JOB_ID, job.id); // only jobId
                    startActivity(intent);
                });

                recyclerView.setAdapter(adapter);
            });
        }).start();

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
