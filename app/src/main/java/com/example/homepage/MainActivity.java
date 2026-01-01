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
import com.example.madproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_homepage);
        seedSampleJobs();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String email = getIntent().getStringExtra("user_email");
        int userId = getIntent().getIntExtra("userId", -1);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
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

    private void seedSampleJobs() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

            if (db.jobDao().getCount() == 0) {
                List<Job> sampleJobs = new ArrayList<>();
                sampleJobs.add(new Job("Construction Helper", "Local Contractor", "Kuala Lumpur",
                        "Construction", "Manual labor", "RM 80/Day", "1.2 km", "95%"));
                sampleJobs.add(new Job("Home Cleaner", "Private Client", "Petaling Jaya",
                        "Domestic", "Cleaning", "RM 50/Day", "0.5 km", "88%"));
                sampleJobs.add(new Job("Waiter", "Restaurant", "Subang",
                        "Food Service", "Customer Service", "RM 60/Day", "2.0 km", "80%"));

                db.jobDao().insertAll(sampleJobs);
            }
        }).start();
    }

}