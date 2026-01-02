package com.example.homepage;

import android.os.Bundle;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.madproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        // ----------------------------
        // Get user data
        // ----------------------------
        String email = getIntent().getStringExtra("user_email");
        int userId = getIntent().getIntExtra("userId", -1);

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userType = prefs.getString("user_type", "employee");

        // ----------------------------
        // Nav Controller
        // ----------------------------
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment == null) return;

        NavController navController = navHostFragment.getNavController();
        NavGraph navGraph = navController.getNavInflater()
                .inflate(R.navigation.nav_graph_home);

        // ----------------------------
        // Set start destination dynamically
        // ----------------------------
        if ("employer".equals(userType)) {
            navGraph.setStartDestination(R.id.employerHomeFragment);
        } else {
            navGraph.setStartDestination(R.id.employeeHomeFragment);
        }

        // Pass data to fragments
        Bundle bundle = new Bundle();
        bundle.putString("user_email", email);
        bundle.putInt("userId", userId);
        bundle.putString("user_type", userType);

        navController.setGraph(navGraph, bundle);

        // Bottom nav setup
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController =
                NavHostFragment.findNavController(
                        getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)
                );
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
