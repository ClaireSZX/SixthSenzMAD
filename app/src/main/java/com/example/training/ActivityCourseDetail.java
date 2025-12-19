package com.example.training;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madproject.R;

public class ActivityCourseDetail extends AppCompatActivity {

    private TextView textDetailTitle;
    private ProgressBar progressBarContent;
    private WebView webViewCourseContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        textDetailTitle = findViewById(R.id.text_detail_title);
        progressBarContent = findViewById(R.id.progress_bar_content);
        webViewCourseContent = findViewById(R.id.web_view_course_content);

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

        // Get data from Intent
        String title = getIntent().getStringExtra("title");
        String contentUrl = getIntent().getStringExtra("content_url"); // URL of course content

        if (title != null) {
            textDetailTitle.setText(title);
        }

        if (contentUrl != null) {
            webViewCourseContent.loadUrl(contentUrl);
        }

        ImageButton btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            finish();
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
