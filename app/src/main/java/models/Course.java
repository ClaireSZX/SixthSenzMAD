package com.example.sixthsenzM5.models;

public class Course {
    private String courseID;
    private String title;
    private String description;
    private String category; //ex: "Interview Skills", "Basic Excel"
    private String duration;
    private String thumbnailUrl; //URL for the course preview page(online)
    private int thumbnailResID = -1; //local drawable images
    private String contentUrl; //URL for the video or external web page

    public Course(){}

    public Course(String courseID, String title, String description, String category, String duration, String thumbnailUrl, String contentUrl){
        this.courseID=courseID;
        this.title=title;
        this.description=description;
        this.category=category;
        this.duration=duration;
        this.thumbnailUrl=thumbnailUrl;
        this.contentUrl=contentUrl;
    }

    public Course(String courseID, String title, String description, String category, String duration, int thumbnailResID, String contentUrl) {
        this.courseID = courseID;
        this.title = title;
        this.description = description;
        this.category = category;
        this.duration = duration;
        this.thumbnailResID = thumbnailResID;
        this.contentUrl = contentUrl;
    }

    //Getters
    public String getCourseID(){
        return courseID;
    }
    public String getTitle(){
        return title;
    }
    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getDuration() {
        return duration;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public int getThumbnailResID() {
        return thumbnailResID;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    //Setters

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setThumbnailResID(int thumbnailResID) {
        this.thumbnailResID = thumbnailResID;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }


}
