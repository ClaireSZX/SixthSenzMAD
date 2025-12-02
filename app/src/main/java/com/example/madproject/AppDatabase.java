package com.example.madproject;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// entities 数组里列出所有的表，version 是数据库版本号，每次修改表结构都需要增加版本号
@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // 让数据库知道它管理着哪个 DAO
    public abstract UserDao userDao();

    // 我们使用单例模式，确保整个应用只有一个数据库实例
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "job_match_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

