package com.example.training;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AppDatabase;
import com.example.madproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ForumActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_COMMENT = 1001;

    private RecyclerView recyclerView;
    private ForumPostAdapter adapter;
    private List<ForumPost> postList = new ArrayList<>();

    private AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_activity);

        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Forum");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerForumPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ForumPostAdapter(postList, this::openCommentScreen);
        recyclerView.setAdapter(adapter);

        db = AppDatabase.getDatabase(this);

        String selectedCourseId = getIntent().getStringExtra("course_id");

        Log.d("ForumActivity", "Selected course ID: " + selectedCourseId);
        db.forumPostDao().getPostsForCourse(selectedCourseId)
                .observe(this, posts -> {
                    Log.d("ForumActivity", "Posts count: " + posts.size());
                });

        db.forumPostDao().getPostsForCourse(selectedCourseId)
                .observe(this, posts -> {
                    postList.clear();
                    postList.addAll(posts);
                    adapter.notifyDataSetChanged();
                });

        FloatingActionButton fab = findViewById(R.id.fabAddPost);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(ForumActivity.this, NewPostActivity.class);
            intent.putExtra("course_id", selectedCourseId); // pass courseId to new post
            startActivity(intent);
        });

        // Seed sample data for testing
        seedSampleData();
    }


    // ───────── Open comments ─────────
    private void openCommentScreen(ForumPost post) {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra("post_id", post.getPostId());
        startActivityForResult(intent, REQUEST_CODE_COMMENT);
    }

    // ───────── Handle comment result ─────────
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_COMMENT && resultCode == RESULT_OK && data != null) {
            String postId = data.getStringExtra("post_id");

            for (int i = 0; i < postList.size(); i++) {
                ForumPost post = postList.get(i);
                if (post.getPostId().equals(postId)) {
                    post.setCommentCount(post.getCommentCount() + 1);
                    adapter.notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    private void seedSampleData() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);

            String[] courseIds = {"course1", "course2"};

            for (String courseId : courseIds) {
                // Use synchronous count
                if (db.forumPostDao().countPostsForCourse(courseId) == 0) {
                    for (int i = 1; i <= 2; i++) {
                        ForumPost post = new ForumPost(
                                UUID.randomUUID().toString(),
                                courseId,
                                "Author " + i,
                                "Sample post " + i + " for " + courseId,
                                System.currentTimeMillis(),
                                0
                        );
                        db.forumPostDao().insert(post);
                    }
                }
            }
        }).start();
    }

}
