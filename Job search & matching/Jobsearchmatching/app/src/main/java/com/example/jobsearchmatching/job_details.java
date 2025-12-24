package com.example.jobsearchmatching;

public class job_details {
    private String title;
    private String category;
    private String payRate;
    private String distance;
    private String matchScore;

    public job_details(String title, String category, String payRate, String distance, String matchScore) {
        this.title = title;
        this.category = category;
        this.payRate = payRate;
        this.distance = distance;
        this.matchScore = matchScore;
    }

    // Getters
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getPayRate() { return payRate; }
    public String getDistance() { return distance; }
    public String getMatchScore() { return matchScore; }
}