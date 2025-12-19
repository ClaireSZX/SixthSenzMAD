package com.example.training;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.madproject.R;

public class NewPostActivity extends AppCompatActivity {

    private EditText editTextPostContent;
    private Button buttonSubmitPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_new_post);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish()); // Back button

        // Initialize views
        editTextPostContent = findViewById(R.id.edit_text_post_content);
        buttonSubmitPost = findViewById(R.id.button_submit_post);

        // Handle submit click
        buttonSubmitPost.setOnClickListener(v -> {
            String content = editTextPostContent.getText().toString().trim();

            if (content.isEmpty()) {
                Toast.makeText(NewPostActivity.this, "Post cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Send result back to ForumActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("new_post_id", String.valueOf(System.currentTimeMillis()));
            resultIntent.putExtra("new_post_author", "Student"); // or dynamic author
            resultIntent.putExtra("new_post_content", content);
            resultIntent.putExtra("new_post_timestamp", System.currentTimeMillis());
            resultIntent.putExtra("new_post_comment_count", 0);

            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
