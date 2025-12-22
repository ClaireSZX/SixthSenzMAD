package com.example.madproject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao // 告诉 Room 这是一个 DAO 接口
public interface UserDao {

    // 插入一个新用户
    @Insert
    void insert(User user);

    // 根据邮箱查找用户 (用于登录验证)
    @Query("SELECT * FROM user_table WHERE email = :email COLLATE NOCASE AND password = :password LIMIT 1")
    User login(String email, String password);

    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    User findByEmail(String email);

    // ✅ 新增的方法：根据 ID 查找用户 (这是解决错误的关键)
    //    这个方法被 ProfileFragment 和 Edit...Fragment 用来获取当前登录用户的数据
    @Query("SELECT * FROM user_table WHERE id = :id LIMIT 1")
    User findById(int id);

    // 更新一个已存在的用户信息 (用于保存编辑后的资料)
    @Update
    void update(User user);

    @Query("SELECT * FROM user_table WHERE id = :id")
    LiveData<User> getUserLiveData(int id);

}


