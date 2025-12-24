package com.example.training;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.AppDatabase;
import com.example.madproject.R;

public class NewPostActivity extends AppCompatActivity {

    private EditText editTextPostContent;
    private Button buttonSubmitPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        // ───────── Toolbar ─────────
        Toolbar toolbar = findViewById(R.id.toolbar_new_post);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        // ───────── Views ─────────
        editTextPostContent = findViewById(R.id.edit_text_post_content);
        buttonSubmitPost = findViewById(R.id.button_submit_post);

        // Get course ID from ForumActivity
        int courseId = getIntent().getIntExtra("course_id",0);

        // ───────── Submit Post ─────────
        buttonSubmitPost.setOnClickListener(v -> {
            String content = editTextPostContent.getText().toString().trim();

            if (content.isEmpty()) {
                Toast.makeText(this, "Post cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (courseId == 0) {
                Toast.makeText(this, "Course not found!", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

                String author = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        .getString("username", "Unknown User");
                ForumPost post = new ForumPost(
                        String.valueOf(System.currentTimeMillis()), // postId
                        courseId,                                  // courseId
                        author,                                 // author (can be dynamic later)
                        content,                                   // content
                        System.currentTimeMillis(),                // timestamp
                        0                                          // commentCount
                );

                db.forumPostDao().insert(post);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Post added", Toast.LENGTH_SHORT).show();
                    finish(); // LiveData will auto-update ForumActivity
                });
            }).start();
        });
    }


}
