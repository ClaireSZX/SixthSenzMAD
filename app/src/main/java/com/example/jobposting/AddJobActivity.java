package com.example.jobposting;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddJobActivity extends AppCompatActivity {

    EditText etTitle, etCompany, etDescription;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        etTitle = findViewById(R.id.etTitle);
        etCompany = findViewById(R.id.etCompany);
        etDescription = findViewById(R.id.etDescription);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            Job job = new Job(
                    etTitle.getText().toString(),
                    etCompany.getText().toString(),
                    etDescription.getText().toString()
            );

            AppDatabase.getInstance(this).jobDao().insert(job);
            finish();
        });
    }
}
