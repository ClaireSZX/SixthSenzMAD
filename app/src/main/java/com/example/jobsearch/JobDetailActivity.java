package com.example.jobsearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.AppDatabase;
import com.example.chatlist.ChatBox;
import com.example.madproject.R;
import com.google.android.material.button.MaterialButton;

public class JobDetailActivity extends AppCompatActivity {

    public static final String EXTRA_JOB_ID = "job_id";

    private TextView tvTitle, tvCompany, tvCategory, tvPay, tvDistance, tvScore, tvDescription;
    private MaterialButton btnChatEmployer, btnDeleteEmployer;
    private String companyName;
    private int jobId = -1;
    private int employerId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        // -------------------
        // Initialize views
        // -------------------
        tvTitle = findViewById(R.id.tvJobTitle);
        tvCompany = findViewById(R.id.tvJobCompany);
        tvCategory = findViewById(R.id.tvCategory);
        tvPay = findViewById(R.id.tvPayRate);
        tvDistance = findViewById(R.id.tvDistance);
        tvScore = findViewById(R.id.tvMatchScore);
        tvDescription = findViewById(R.id.tvDescription);

        btnChatEmployer = findViewById(R.id.btnChatEmployer);
        btnDeleteEmployer = findViewById(R.id.btnDeleteEmployer);

        // Hide delete button by default
        btnDeleteEmployer.setVisibility(View.GONE);

        // -------------------
        // Back button
        // -------------------
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // -------------------
        // Get jobId from Intent
        // -------------------
        Intent intent = getIntent();
        if (intent != null) {
            jobId = intent.getIntExtra(EXTRA_JOB_ID, -1);
        }

        if (jobId == -1) {
            Toast.makeText(this, "Job ID not provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // -------------------
        // Fetch job from database
        // -------------------
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(JobDetailActivity.this);
            Job job = db.jobDao().getJobById(jobId);

            runOnUiThread(() -> {
                if (job != null) {
                    displayJobDetails(job);
                } else {
                    Toast.makeText(JobDetailActivity.this, "Job not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }).start();
    }

    // -------------------
    // Display job details on UI
    // -------------------
    private void displayJobDetails(Job job) {
        tvTitle.setText(job.getTitle() != null ? job.getTitle() : "No title");
        tvCompany.setText(job.getCompany() != null ? job.getCompany() : "No company");
        tvCategory.setText(job.getIndustry() != null ? job.getIndustry() : "No category");
        tvPay.setText(job.getPayRate() != null ? job.getPayRate() : "N/A");
        tvDescription.setText(job.getDescription() != null ? job.getDescription() : "No description");

        tvDistance.setText(job.getDistanceText());  // use helper
        tvScore.setText(job.getMatchScoreText());   // use helper

        companyName = job.getCompany();
        employerId = job.getEmployerId();

        // -------------------
        // Show delete button only if logged-in employer owns this job
        // -------------------
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userType = prefs.getString("user_type", "employee");
        int loggedInUserId = prefs.getInt("user_id", -1);

        if ("employer".equalsIgnoreCase(userType) && loggedInUserId == employerId) {
            btnDeleteEmployer.setVisibility(View.VISIBLE);
        }

        // -------------------
        // Chat button
        // -------------------
        btnChatEmployer.setOnClickListener(v -> {
            if (companyName != null && !companyName.isEmpty()) {
                Intent chatIntent = new Intent(JobDetailActivity.this, ChatBox.class);
                chatIntent.putExtra("company_name", companyName);
                startActivity(chatIntent);
            } else {
                Toast.makeText(JobDetailActivity.this,
                        "Cannot start chat: Company info missing", Toast.LENGTH_SHORT).show();
            }
        });

        // -------------------
        // Delete button
        // -------------------
        btnDeleteEmployer.setOnClickListener(v -> {
            new Thread(() -> {
                AppDatabase db = AppDatabase.getDatabase(JobDetailActivity.this);
                db.jobDao().deleteJobById(jobId);

                runOnUiThread(() -> {
                    Toast.makeText(JobDetailActivity.this, "Job deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
