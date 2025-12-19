package com.example.training;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private EditText editTextComment;
    private Button buttonPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);

        Toolbar toolbar = findViewById(R.id.toolbar_post_detail);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Comments");
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        TextView tvAuthor = findViewById(R.id.text_post_detail_author);
        TextView tvContent = findViewById(R.id.text_post_detail_content);

        String author = getIntent().getStringExtra("post_author");
        String content = getIntent().getStringExtra("post_content");
        String postId = getIntent().getStringExtra("post_id");

        tvAuthor.setText(author);
        tvContent.setText(content);

        recyclerView = findViewById(R.id.recycler_view_comments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        editTextComment = findViewById(R.id.edit_text_comment);
        buttonPost = findViewById(R.id.button_post_comment);

        buttonPost.setOnClickListener(v -> {
            String commentText = editTextComment.getText().toString().trim();
            if (!commentText.isEmpty()) {
                Comment comment = new Comment(commentText, "Student");
                adapter.addComment(comment);
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                editTextComment.setText("");

                // Return to ForumActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("post_id", postId);
                resultIntent.putExtra("comment_author", comment.getAuthor());
                resultIntent.putExtra("comment_content", comment.getContent());
                setResult(RESULT_OK, resultIntent);
            }
        });
    }
}


