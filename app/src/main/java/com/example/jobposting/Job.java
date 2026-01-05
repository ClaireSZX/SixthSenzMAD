package com.example.jobposting;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Job {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String company;
    public String description;

    public Job(String title, String company, String description) {
        this.title = title;
        this.company = company;
        this.description = description;
    }
}
