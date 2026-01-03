package com.example.jobsearch;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "job")
public class Job {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String company;
    public String location;
    public String industry;
    public String skills;

    public String payRate;
    public String distance;
    public String matchScore;
    public int employerId;

    public Job(String title, String company, String location, String industry,
               String skills, String payRate, String distance,
               String matchScore, int employerId) {

        this.title = title;
        this.company = company;
        this.location = location;
        this.industry = industry;
        this.skills = skills;
        this.payRate = payRate;
        this.distance = distance;
        this.matchScore = matchScore;
        this.employerId = employerId;
    }

    public String getTitle() { return title; }
    public String getCompany() { return company; }
    public String getIndustry() { return industry; }
    public String getPayRate() { return payRate; }
    public String getDistance() { return distance; }
    public String getMatchScore() { return matchScore; }
    public int getEmployerId() { return employerId; }
}
