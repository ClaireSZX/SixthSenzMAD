package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.homepage.MainActivity;

public class AppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            int userId = data.getIntExtra("userId", -1);
            if (userId != -1) {
                // Open the main activity only after signup
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        }
    }
}



