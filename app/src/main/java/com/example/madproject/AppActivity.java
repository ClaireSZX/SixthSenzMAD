// AppActivity.java
// 它的职责是接收 userId，然后传递给 ProfileFragment
package com.example.madproject;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class AppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        // 从 Intent 中获取用户的 ID
        int userId = getIntent().getIntExtra("userId", -1);
        if (userId == -1) {
            // 如果没有有效的 userId，则不应该进入这个页面
            Toast.makeText(this, "Error: User not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ProfileFragment profileFragment = new ProfileFragment();

        // 将 userId 传递给 ProfileFragment
        Bundle args = new Bundle();
        args.putInt("userId", userId);
        profileFragment.setArguments(args);

        // 加载 ProfileFragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.temporary_container, profileFragment);
        fragmentTransaction.commit();
    }
}




