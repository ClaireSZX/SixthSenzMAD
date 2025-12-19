package com.example.training;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ForumActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ForumPostAdapter adapter;
    private List<ForumPost> postList;
    private static final int REQUEST_CODE_COMMENT = 2001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_activity);

        recyclerView = findViewById(R.id.recyclerForumPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postList = new ArrayList<>();
        loadSamplePosts();

        adapter = new ForumPostAdapter(postList, post -> {
            Intent intent = new Intent(this, CommentActivity.class);
            intent.putExtra("post_id", post.getPostId());
            intent.putExtra("post_author", post.getAuthor());
            intent.putExtra("post_content", post.getContent());
            startActivityForResult(intent, REQUEST_CODE_COMMENT);
        });
        recyclerView.setAdapter(adapter);

        ImageButton backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(v -> finish());

        FloatingActionButton fab = findViewById(R.id.fabAddPost);
        fab.setOnClickListener(v -> {
            // Launch NewPostActivity
        });
    }

    private void loadSamplePosts() {
        postList.add(new ForumPost("1", "Alice", "Hello!", System.currentTimeMillis(), 0));
        postList.add(new ForumPost("2", "Bob", "Welcome to forum", System.currentTimeMillis(), 0));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_COMMENT && resultCode == RESULT_OK && data != null) {
            String postId = data.getStringExtra("post_id");
            String commentAuthor = data.getStringExtra("comment_author");
            String commentContent = data.getStringExtra("comment_content");

            // Update comment count
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
}
