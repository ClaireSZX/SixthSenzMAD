package com.example.madproject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "job")
public class Job {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String company;
    public String location;
    public String skills;
    public String industry;

    public Job(String title, String company, String location, String skills, String industry) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.skills = skills;
        this.industry = industry;
    }
}
