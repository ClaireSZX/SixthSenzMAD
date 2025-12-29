package com.example.training;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AppDatabase;
import com.example.madproject.R;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private EditText editTextComment;
    private Button buttonPost;

    private AppDatabase db;
    private String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);

        // ───────── Toolbar ─────────
        Toolbar toolbar = findViewById(R.id.toolbar_post_detail);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Comments");
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        // ───────── Post details ─────────
        TextView tvAuthor = findViewById(R.id.text_post_detail_author);
        TextView tvContent = findViewById(R.id.text_post_detail_content);

        String sampleAuthor = getIntent().getStringExtra("post_author");
        String content = getIntent().getStringExtra("post_content");
        postId = getIntent().getStringExtra("post_id");

        tvAuthor.setText(sampleAuthor);
        tvContent.setText(content);

        if (postId == null) {
            Toast.makeText(this, "Post not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ───────── RecyclerView ─────────
        recyclerView = findViewById(R.id.recycler_view_comments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentAdapter();
        recyclerView.setAdapter(adapter);

        // ───────── Database ─────────
        db = AppDatabase.getDatabase(this);

        // Observe comments for this post
        db.commentDao()
                .getCommentsForPost(postId)
                .observe(this, comments -> {
                    adapter.setComments(comments);
                    recyclerView.scrollToPosition(
                            Math.max(comments.size() - 1, 0)
                    );
                });

        // ───────── Input ─────────
        editTextComment = findViewById(R.id.edit_text_comment);
        buttonPost = findViewById(R.id.button_post_comment);

        buttonPost.setOnClickListener(v -> {
            String commentText = editTextComment.getText().toString().trim();
            if (commentText.isEmpty()) return;

            String author = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    .getString("username", "Unknown User");

            Comment comment = new Comment(
                    postId,
                    author,
                    commentText,
                    System.currentTimeMillis()
            );

            new Thread(() -> {
                // Insert comment
                db.commentDao().insert(comment);

                // Increment comment count in ForumPost
                ForumPost post = db.forumPostDao().findPostById(postId);
                if (post != null) {
                    post.setCommentCount(post.getCommentCount() + 1);
                    db.forumPostDao().update(post);
                }
            }).start();

            editTextComment.setText("");
        });

    }
}
