package com.example.training;

public class Course {
    private String id;
    private String title;
    private String category;
    private String duration;
    private String contentUrl; // optional for WebView

    public Course(String id, String title, String category, String duration, String contentUrl) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.duration = duration;
        this.contentUrl = contentUrl;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getDuration() { return duration; }
    public String getContentUrl() { return contentUrl; }
}
