package com.example.homepage;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.AppDatabase;
import com.example.jobsearch.Job;
import com.example.jobsearch.JobDao;
import com.example.madproject.R;
import com.example.madproject.User;
import com.example.madproject.UserDao;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_homepage);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String email = getIntent().getStringExtra("user_email");
        int userId = getIntent().getIntExtra("userId", -1);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
        }
        NavController navController = navHostFragment.getNavController();

        Bundle bundle = new Bundle();
        bundle.putString("user_email", email);
        bundle.putInt("userId", userId);

// Set graph with initial arguments BEFORE setting up bottom nav
        navController.setGraph(R.navigation.nav_graph_home, bundle);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNav, navController);



    }
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }



}