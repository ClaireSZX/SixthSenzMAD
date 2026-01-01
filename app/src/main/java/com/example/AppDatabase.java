package com.example;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.jobsearch.Job;
import com.example.jobsearch.JobDao;
import com.example.madproject.User;
import com.example.madproject.UserDao;
import com.example.training.CommentDao;
import com.example.training.Course;
import com.example.training.CourseDao;
import com.example.training.ForumPost;
import com.example.training.ForumPostDao;
import com.example.training.Comment;

import java.util.concurrent.Executors;


// entities 数组里列出所有的表，version 是数据库版本号，每次修改表结构都需要增加版本号
@Database(
        entities = {ForumPost.class, User.class, Comment.class, Course.class, Job.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract ForumPostDao forumPostDao();
    public abstract UserDao userDao();
    public abstract CommentDao commentDao();
    public abstract CourseDao courseDao();
    public abstract JobDao jobDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "app_db"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
