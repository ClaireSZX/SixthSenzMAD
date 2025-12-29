package com.example.training;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.AppDatabase;
import com.example.madproject.R;

import java.util.UUID;

public class AddCourseActivity extends AppCompatActivity {

    private EditText etTitle, etCategory, etDuration, etContentUrl;
    private Button btnSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course_activity);

        etTitle = findViewById(R.id.etTitle);
        etCategory = findViewById(R.id.etCategory);
        etDuration = findViewById(R.id.etDuration);
        etContentUrl = findViewById(R.id.etContentUrl);
        btnSave = findViewById(R.id.btnSaveCourse);

        btnSave.setOnClickListener(v -> saveCourse());
    }

    private void saveCourse() {
        String title = etTitle.getText().toString().trim();
        String category = etCategory.getText().toString().trim();
        String duration = etDuration.getText().toString().trim();
        String contentUrl = etContentUrl.getText().toString().trim();

        if (title.isEmpty() || category.isEmpty() || duration.isEmpty() || contentUrl.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        Course newCourse = new Course(title, category, duration, contentUrl);

        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            db.courseDao().insertCourse(newCourse);

            runOnUiThread(() -> {
                Toast.makeText(this, "Course saved successfully!", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).start();
    }

}
