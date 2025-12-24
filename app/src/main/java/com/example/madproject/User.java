package com.example.madproject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table") // 告诉 Room 这个类对应数据库中的 "user_table" 表
public class User {

    @PrimaryKey(autoGenerate = true) // 将 id 设为主键，并让数据库自动生成
    public int id;

    // 基本信息
    public String email;
    public String password;
    public String userType; // "employee" or "employer"

    // 员工信息
    public String fullName;
    public String workingExperience;
    public String skills;
    public String language;
    public String phone;

    // 雇主信息
    public String companyName;
    public String aboutCompany;
    public String companySize;
    public String industry;
    public String location;

    // Room 需要一个空的构造函数
    public User() {}

    public String getEmail() {
        return this.email;
    }

    public String getUserType(){
        return this.userType;
    }

    public String getName() {
        if(userType.equals("employee")) {
            return fullName;
        } else {
            return companyName;
        }
    }
}

