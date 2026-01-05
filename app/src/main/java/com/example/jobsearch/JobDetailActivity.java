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

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_CATEGORY = "category";
    public static final String EXTRA_PAY = "pay";
    public static final String EXTRA_DISTANCE = "distance";
    public static final String EXTRA_SCORE = "score";
    public static final String EXTRA_COMPANY = "company";
    public static final String EXTRA_JOB_ID = "job_id";
    public static final String EXTRA_EMPLOYER_ID = "employer_id";

    public static final String EXTRA_DESCRIPTION = "description";

    private String companyName;
    private int jobId = -1;
    private int employerId = -1;

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
        TextView tvTitle = findViewById(R.id.tvJobTitle);
        TextView tvCompany = findViewById(R.id.tvJobCompany);
        TextView tvCategory = findViewById(R.id.tvCategory);
        TextView tvPay = findViewById(R.id.tvPayRate);
        TextView tvDistance = findViewById(R.id.tvDistance);
        TextView tvScore = findViewById(R.id.tvMatchScore);
        TextView tvDescription = findViewById(R.id.tvDescription);


        MaterialButton btnChatEmployer = findViewById(R.id.btnChatEmployer);
        MaterialButton btnDeleteEmployer = findViewById(R.id.btnDeleteEmployer);

        // Hide delete button by default
        btnDeleteEmployer.setVisibility(View.GONE);

        // -------------------
        // Get logged-in user info
        // -------------------
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userType = prefs.getString("user_type", "employee"); // default
        int loggedInUserId = prefs.getInt("user_id", -1);

        // -------------------
        // Get intent data
        // -------------------
        Intent intent = getIntent();
        if (intent != null) {
            tvTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            tvCompany.setText(intent.getStringExtra(EXTRA_COMPANY));
            tvCategory.setText(intent.getStringExtra(EXTRA_CATEGORY));
            tvPay.setText(intent.getStringExtra(EXTRA_PAY));

            tvDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));


            // Distance and match score might be numeric
            double distance = intent.getDoubleExtra(EXTRA_DISTANCE, 0.0);
            tvDistance.setText(String.format("%.1f km", distance));

            String matchScore = intent.getStringExtra(EXTRA_SCORE);
            if (matchScore == null || matchScore.isEmpty()) {
                matchScore = "0%";
            }
            tvScore.setText(matchScore);

            companyName = intent.getStringExtra(EXTRA_COMPANY);
            jobId = intent.getIntExtra(EXTRA_JOB_ID, -1);
            employerId = intent.getIntExtra(EXTRA_EMPLOYER_ID, -1);
        }

        // -------------------
        // Show delete only if logged-in employer owns this job
        // -------------------
        if ("employer".equalsIgnoreCase(userType) && loggedInUserId == employerId) {
            btnDeleteEmployer.setVisibility(View.VISIBLE);
        }

        // -------------------
        // Chat with employer
        // -------------------
        btnChatEmployer.setOnClickListener(v -> {
            if (companyName != null) {
                Intent chatIntent = new Intent(JobDetailActivity.this, ChatBox.class);
                chatIntent.putExtra("company_name", companyName);
                startActivity(chatIntent);
            }
        });

        // -------------------
        // Delete job
        // -------------------
        btnDeleteEmployer.setOnClickListener(v -> {
            if (jobId != -1) {
                new Thread(() -> {
                    AppDatabase db = AppDatabase.getDatabase(JobDetailActivity.this);
                    db.jobDao().deleteJobById(jobId);

                    runOnUiThread(() -> {
                        Toast.makeText(JobDetailActivity.this,
                                "Job deleted successfully", Toast.LENGTH_SHORT).show();
                        finish(); // close after deletion
                    });
                }).start();
            } else {
                Toast.makeText(this, "Cannot delete this job", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
