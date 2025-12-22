package com.example.training;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.madproject.R;

public class ActivityCourseDetail extends AppCompatActivity {

    private ProgressBar progressBarContent;
    private WebView webViewCourseContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_course_detail);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        // Views
        progressBarContent = findViewById(R.id.progress_bar_content);
        webViewCourseContent = findViewById(R.id.web_view_course_content);
        Button goToForumButton = findViewById(R.id.goToForumButton);

        // Intent data
        String courseTitle = getIntent().getStringExtra("title");
        String contentUrl = getIntent().getStringExtra("content_url");
        String courseId = getIntent().getStringExtra("course_id");

        // Set toolbar title from course card
        if (courseTitle != null && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(courseTitle);
        }

        // WebView settings
        WebSettings settings = webViewCourseContent.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        webViewCourseContent.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(
                    WebView view,
                    WebResourceRequest request,
                    WebResourceError error
            ) {
                Log.e("WEBVIEW", "Error: " + error.getDescription());
            }
        });

        webViewCourseContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBarContent.setVisibility(
                        newProgress < 100 ? View.VISIBLE : View.GONE
                );
            }
        });

        if (contentUrl != null) {
            webViewCourseContent.loadUrl(contentUrl);
        }


        goToForumButton.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityCourseDetail.this, ForumActivity.class);
            intent.putExtra("course_id", courseId);
            intent.putExtra("course_title", courseTitle);

            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        if (webViewCourseContent.canGoBack()) {
            webViewCourseContent.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
