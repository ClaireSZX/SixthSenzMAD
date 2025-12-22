package com.example.sixthsenzM5.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sixthsenzM5.R;
import com.example.sixthsenzM5.adapters.CommentAdapter;
import com.example.sixthsenzM5.models.Comment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    private static final String TAG = "PostDetailActivity";

    private TextView postUsernameTextView, postContentTextView;
    private EditText commentEditText;
    private Button postCommentButton;
    private RecyclerView commentsRecyclerView;

    private String postID;
    private FirebaseFirestore db;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private ListenerRegistration commentsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // Correct Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_post_detail);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Post Details");
        }

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Initialize Views
        postUsernameTextView = findViewById(R.id.text_post_detail_username);
        postContentTextView = findViewById(R.id.text_post_detail_content);
        commentEditText = findViewById(R.id.edit_text_comment);
        postCommentButton = findViewById(R.id.button_post_comment);
        commentsRecyclerView = findViewById(R.id.recycler_view_comments);

        // Get post data
        postID = getIntent().getStringExtra("POST_ID");
        String postContent = getIntent().getStringExtra("POST_CONTENT");
        String postUsername = getIntent().getStringExtra("POST_USERNAME");

        if (postID == null) {
            Toast.makeText(this, "Error: Post ID is missing.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        postUsernameTextView.setText(postUsername);
        postContentTextView.setText(postContent);

        // Setup RecyclerView
        db = FirebaseFirestore.getInstance();
        commentList = new ArrayList<>();
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(this, commentList);
        commentsRecyclerView.setAdapter(commentAdapter);

        startCommentsListener();

        postCommentButton.setOnClickListener(v -> submitComment());
    }

    // Real-time comments listener
    private void startCommentsListener() {
        commentsListener = db.collection("comments")
                .whereEqualTo("postID", postID)
                .orderBy("commentID") // Lexicographic, may need numeric field for proper order
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Comments listen failed.", e);
                        return;
                    }

                    if (snapshots != null) {
                        commentList.clear();
                        for (DocumentSnapshot doc : snapshots.getDocuments()) {
                            Comment comment = doc.toObject(Comment.class);
                            if (comment != null) {
                                comment.setCommentID(doc.getId());
                                commentList.add(comment);
                            }
                        }
                        commentAdapter.notifyDataSetChanged();
                        // Auto-scroll to latest comment
                        if (!commentList.isEmpty()) {
                            commentsRecyclerView.scrollToPosition(commentList.size() - 1);
                        }
                    }
                });
    }

    // Submit comment
    private void submitComment() {
        String commentText = commentEditText.getText().toString().trim();
        if (commentText.isEmpty()) {
            Toast.makeText(this, "Please enter a comment.", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentUserId = "user_456"; // TODO: Replace with Firebase Auth
        String currentUsername = "CommunityMember"; // TODO: Replace with Auth username

        // Generate new ID safely (simple method)
        db.collection("comments")
                .orderBy("commentId")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    String newCommentId = generateNextCommentId(querySnapshot);

                    Comment newComment = new Comment(
                            newCommentId,
                            postID,
                            currentUserId,
                            currentUsername,
                            commentText,
                            System.currentTimeMillis()
                    );

                    db.collection("comments")
                            .document(newCommentId)
                            .set(newComment)
                            .addOnSuccessListener(aVoid -> {
                                commentEditText.setText("");
                                Toast.makeText(this, "Comment posted!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Log.w(TAG, "Error posting comment", e);
                                Toast.makeText(this, "Failed to post comment.", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error fetching comments for ID generation", e);
                    Toast.makeText(this, "Failed to generate comment ID.", Toast.LENGTH_SHORT).show();
                });
    }

    private String generateNextCommentId(QuerySnapshot snapshot) {
        int max = 0;
        for (DocumentSnapshot doc : snapshot) {
            String id = doc.getString("commentId");
            if (id != null && id.startsWith("R")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > max) max = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("R%03d", max + 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (commentsListener != null) {
            commentsListener.remove();
        }
    }
}
