package com.example.sixthsenzM5.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sixthsenzM5.R;

public class CourseDetailsActivity extends AppCompatActivity {

    private WebView webView;
    private TextView titleTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // Initialize views
        webView = findViewById(R.id.web_view_course_content);
        titleTextView = findViewById(R.id.text_detail_title);
        progressBar = findViewById(R.id.progress_bar_content);

        // Retrieve data passed via Intent
        Intent intent = getIntent();
        if (intent != null) {
            String courseTitle = intent.getStringExtra("COURSE_TITLE");
            String contentUrl = intent.getStringExtra("CONTENT_URL");

            titleTextView.setText(courseTitle != null ? courseTitle : "Course Details");

            if (contentUrl != null) {
                displayCourseContent(contentUrl);
            } else {
                Toast.makeText(this, "Error: Course content URL missing.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void displayCourseContent(String url) {
        // If it's YouTube → open YouTube app
        if (url.contains("youtube.com") || url.contains("youtu.be")) {
            Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            try {
                startActivity(youtubeIntent);
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Could not open video. Check link.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise load into WebView
            loadContentInWebView(url);
        }
    }

    private void loadContentInWebView(String url) {
        webView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(CourseDetailsActivity.this, "Failed to load content: " + description, Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });

        webView.loadUrl(url);
    }
}

