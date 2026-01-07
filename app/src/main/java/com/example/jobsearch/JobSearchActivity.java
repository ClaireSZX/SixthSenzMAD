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

        // Attach map fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map_container, new MapsFragment())
                    .commit();
        }

        new Thread(() -> {
            List<Job> jobList = db.jobDao().getAllJobs();

            runOnUiThread(() -> {

                // RecyclerView
                adapter = new JobAdaptor(jobList, job -> {
                    Intent intent = new Intent(this, JobDetailActivity.class);
                    intent.putExtra("job_id", job.id);
                    startActivity(intent);
                });

                recyclerView.setAdapter(adapter);

                // 🔥 SEND JOBS TO MAP
                MapsFragment mapFragment =
                        (MapsFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map_container);

                if (mapFragment != null) {
                    mapFragment.setJobs(jobList);
                }
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
