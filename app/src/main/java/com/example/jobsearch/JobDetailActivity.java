package com.example.jobsearch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

        Intent intent = getIntent();
        if (intent != null) {
            tvTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            tvCompany.setText(intent.getStringExtra(EXTRA_COMPANY));
            tvCategory.setText(intent.getStringExtra(EXTRA_CATEGORY));
            tvPay.setText(intent.getStringExtra(EXTRA_PAY));
            tvDistance.setText(intent.getStringExtra(EXTRA_DISTANCE));
            tvScore.setText(intent.getStringExtra(EXTRA_SCORE));

            companyName = intent.getStringExtra(EXTRA_COMPANY);
        }

        MaterialButton btnGoToDetail = findViewById(R.id.btnChatEmployer);
        btnGoToDetail.setOnClickListener(v -> {
            if (companyName != null) {
                Intent intent2 = new Intent(JobDetailActivity.this, ChatBox.class);
                intent2.putExtra("company_name", companyName);
                startActivity(intent2);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
