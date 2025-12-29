package com.example.training;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "Course")
public class Course implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id; // Room uses int for autoGenerate
    private String title;
    private String category;
    private String duration;
    private String contentUrl;

    public Course(String title, String category, String duration, String contentUrl) {
        this.title = title;
        this.category = category;
        this.duration = duration;
        this.contentUrl = contentUrl;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getDuration() { return duration; }
    public String getContentUrl() { return contentUrl; }

    // Parcelable implementation
    protected Course(Parcel in) {
        id = in.readInt();
        title = in.readString();
        category = in.readString();
        duration = in.readString();
        contentUrl = in.readString();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(category);
        dest.writeString(duration);
        dest.writeString(contentUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
