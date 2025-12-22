package com.example.sixthsenzM5.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.example.sixthsenzM5.R;
import com.example.sixthsenzM5.models.ForumPost;

import java.util.ArrayList;
import java.util.List;

public class NewPostActivity extends AppCompatActivity {

    private EditText contentEditText;
    private Button submitButton;
    private FirebaseFirestore db;
    private static final String TAG = "NewPostActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        // Set up toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_new_post);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back arrow
            getSupportActionBar().setTitle("New Post");           // Optional title
        }

        // Handle back button click
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Initialize views
        contentEditText = findViewById(R.id.edit_text_post_content);
        submitButton = findViewById(R.id.button_submit_post);
        db = FirebaseFirestore.getInstance();

        submitButton.setOnClickListener(v -> submitNewPost());
    }


    private void submitNewPost() {
        String postContent = contentEditText.getText().toString().trim();

        if (TextUtils.isEmpty(postContent)) {
            Toast.makeText(this, "Please enter content for your post.", Toast.LENGTH_SHORT).show();
            return;
        }

        submitButton.setEnabled(false);

        String currentUserId = "AUTH_USER_123"; // Replace with actual user ID
        String currentUsername = "JobLinkUser"; // Replace with actual username
        List<String> tags = extractTagsFromContent(postContent);

        // Step 1: Fetch existing posts to determine next PXXX ID
        db.collection("forumPosts")
                .orderBy("postId")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    String newPostId = generateNextPostId(querySnapshot);

                    ForumPost newPost = new ForumPost(
                            newPostId,
                            currentUserId,
                            currentUsername,
                            postContent,
                            System.currentTimeMillis(),
                            tags
                    );

                    // Step 2: Save post with custom ID
                    db.collection("forumPosts")
                            .document(newPostId)
                            .set(newPost)
                            .addOnSuccessListener(aVoid -> {
                                Log.d(TAG, "Post added with ID: " + newPostId);
                                Toast.makeText(this, "Post submitted successfully!", Toast.LENGTH_LONG).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Log.w(TAG, "Error adding post", e);
                                Toast.makeText(this, "Failed to post. Check connection.", Toast.LENGTH_LONG).show();
                                submitButton.setEnabled(true);
                            });
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error fetching posts for ID generation", e);
                    Toast.makeText(this, "Failed to generate post ID.", Toast.LENGTH_LONG).show();
                    submitButton.setEnabled(true);
                });
    }

    // Generate next PXXX post ID
    private String generateNextPostId(QuerySnapshot snapshot) {
        int max = 0;
        for (DocumentSnapshot doc : snapshot) {
            String id = doc.getString("postId"); // e.g., P001
            if (id != null && id.startsWith("P")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("P%03d", max + 1); // e.g., P001, P002
    }

    // Extract hashtags
    private List<String> extractTagsFromContent(String content) {
        List<String> tags = new ArrayList<>();
        String[] words = content.split("\\s+");
        for (String word : words) {
            if (word.startsWith("#") && word.length() > 1) {
                tags.add(word.substring(1).toLowerCase());
            }
        }
        return tags;
    }
}
