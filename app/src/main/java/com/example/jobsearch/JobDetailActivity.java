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

    private String companyName;
    private int jobId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        TextView tvTitle = findViewById(R.id.tvJobTitle);
        TextView tvCompany = findViewById(R.id.tvJobCompany);
        TextView tvCategory = findViewById(R.id.tvCategory);
        TextView tvPay = findViewById(R.id.tvPayRate);
        TextView tvDistance = findViewById(R.id.tvDistance);
        TextView tvScore = findViewById(R.id.tvMatchScore);

        MaterialButton btnChatEmployer = findViewById(R.id.btnChatEmployer);
        MaterialButton btnDeleteEmployer = findViewById(R.id.btnDeleteEmployer);

        // Hide delete button by default
        btnDeleteEmployer.setVisibility(View.GONE);

        // -------------------------------
        // Check user type from SharedPreferences
        // -------------------------------
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userType = prefs.getString("user_type", "employee"); // default to employee

        // Only show delete button for employers
        if ("employer".equalsIgnoreCase(userType)) {
            btnDeleteEmployer.setVisibility(View.VISIBLE);
        }

        Intent intent = getIntent();
        if (intent != null) {
            tvTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            tvCompany.setText(intent.getStringExtra(EXTRA_COMPANY));
            tvCategory.setText(intent.getStringExtra(EXTRA_CATEGORY));
            tvPay.setText(intent.getStringExtra(EXTRA_PAY));
            tvDistance.setText(intent.getStringExtra(EXTRA_DISTANCE));
            tvScore.setText(intent.getStringExtra(EXTRA_SCORE));

            companyName = intent.getStringExtra(EXTRA_COMPANY);
            jobId = intent.getIntExtra("job_id", -1);
        }

        btnChatEmployer.setOnClickListener(v -> {
            if (companyName != null) {
                Intent chatIntent = new Intent(JobDetailActivity.this, ChatBox.class);
                chatIntent.putExtra("company_name", companyName);
                startActivity(chatIntent);
            }
        });

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
