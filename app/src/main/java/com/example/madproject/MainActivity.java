package com.example.madproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 这行代码是 MainActivity 唯一需要做的事：
        // 把 activity_main.xml 文件设置为它的界面。
        setContentView(R.layout.activity_main);
    }
}
