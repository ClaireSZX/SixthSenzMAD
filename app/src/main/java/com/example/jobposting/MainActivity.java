package com.example.jobposting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tvJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvJobs = findViewById(R.id.tvJobs);
        Button btnAddJob = findViewById(R.id.btnAddJob);

        btnAddJob.setOnClickListener(v ->
                startActivity(new Intent(this, AddJobActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Job> jobs = AppDatabase.getInstance(this).jobDao().getAllJobs();
        StringBuilder sb = new StringBuilder();

        for (Job job : jobs) {
            sb.append(job.title).append(" - ")
                    .append(job.company).append("\n")
                    .append(job.description).append("\n\n");
        }

        tvJobs.setText(sb.toString());
    }
}
