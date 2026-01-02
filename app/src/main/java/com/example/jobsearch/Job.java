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

    // UI-related
    public String payRate;
    public String distance;
    public String matchScore;
    public int employerId; // ID of the employer (matches User.id)


    public Job(
            String title,
            String company,
            String location,
            String industry,
            String skills,
            String payRate,
            String distance,
            String matchScore,
            int employerId // ID of the employer (matches User.id)
    ) {
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

    public String getTitle() {
        return title;
    }
}
