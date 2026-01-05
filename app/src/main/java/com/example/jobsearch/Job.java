package com.example.jobsearch;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "job")
public class Job {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String company;
    public String location;     // e.g., "Kuala Lumpur"
    public String industry;
    public String skills;

    public String payRate;
    public double distance;     // store as double for calculation
    public double matchScore;   // store as double (0–100) instead of string
    public int employerId;
    public String description;

    public Job(String title, String company, String location, String industry,
               String skills, String description, String payRate,
               double distance, double matchScore, int employerId) {

        this.title = title;
        this.company = company;
        this.location = location;
        this.industry = industry;
        this.skills = skills;
        this.description = description; // assign description
        this.payRate = payRate;
        this.distance = distance;
        this.matchScore = matchScore;
        this.employerId = employerId;
    }

    // Add getters for description
    public String getDescription() { return description; }

    // Empty constructor for Room
    public Job() {}

    // ======================
    // Getters & Setters
    // ======================
    public String getTitle() { return title; }
    public String getCompany() { return company; }
    public String getLocation() { return location; }
    public String getIndustry() { return industry; }
    public String getSkills() { return skills; }
    public String getPayRate() { return payRate; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

    public double getMatchScore() { return matchScore; }
    public void setMatchScore(double matchScore) { this.matchScore = matchScore; }

    public int getEmployerId() { return employerId; }
    public void setEmployerId(int employerId) { this.employerId = employerId; }

    // Helper method to display distance nicely
    public String getDistanceText() {
        return String.format("%.1f km", distance);
    }

    // Helper method to display matchScore nicely
    public String getMatchScoreText() {
        return String.format("%.0f%%", matchScore);
    }
}
