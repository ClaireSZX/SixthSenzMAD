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

    private int jobId = -1;
    private int employerId = -1;
    private String companyName;

    // Views
    private TextView tvTitle, tvCompany, tvCategory, tvPay,
            tvDistance, tvScore, tvDescription;

    private MaterialButton btnChatEmployer, btnDeleteEmployer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        // -------------------
        // Back button
        // -------------------
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // -------------------
        // Views
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
        btnDeleteEmployer.setVisibility(View.GONE);

        // -------------------
        // Logged-in user
        // -------------------
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userType = prefs.getString("user_type", "employee");
        int loggedInUserId = prefs.getInt("user_id", -1);

        // -------------------
        // Get jobId from intent
        // -------------------
        jobId = getIntent().getIntExtra(EXTRA_JOB_ID, -1);
        if (jobId == -1) {
            Toast.makeText(this, "Invalid job", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // -------------------
        // Load job from DB
        // -------------------
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            Job job = db.jobDao().getJobById(jobId);

            if (job == null) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Job not found", Toast.LENGTH_SHORT).show();
                    finish();
                });
                return;
            }

            employerId = job.getEmployerId();
            companyName = job.getCompany();

            runOnUiThread(() -> {
                tvTitle.setText(job.getTitle());
                tvCompany.setText(job.getCompany());
                tvCategory.setText(job.getIndustry());
                tvPay.setText(job.getPayRate());
                tvDescription.setText(job.getDescription());

                tvDistance.setText(
                        String.format("%.1f km", job.getDistance())
                );

                tvScore.setText(String.format("%.0f%%", job.getMatchScore()));


                // Show delete if owner
                if ("employer".equalsIgnoreCase(userType)
                        && loggedInUserId == employerId) {
                    btnDeleteEmployer.setVisibility(View.VISIBLE);
                }
            });
        }).start();

        // -------------------
        // Chat with employer
        // -------------------
        btnChatEmployer.setOnClickListener(v -> {
            if (companyName != null) {
                Intent chatIntent = new Intent(this, ChatBox.class);
                chatIntent.putExtra("company_name", companyName);
                startActivity(chatIntent);
            }
        });

        // -------------------
        // Delete job
        // -------------------
        btnDeleteEmployer.setOnClickListener(v -> {
            new Thread(() -> {
                AppDatabase db = AppDatabase.getDatabase(this);
                db.jobDao().deleteJobById(jobId);

                runOnUiThread(() -> {
                    Toast.makeText(this,
                            "Job deleted successfully",
                            Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }
}
