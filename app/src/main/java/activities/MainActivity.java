package com.example.sixthsenzM5.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast; // Kept Toast import, though currently unused

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.sixthsenzM5.R;
// Import ALL necessary Fragment classes
import com.example.sixthsenzM5.fragments.HomeFragment; // **NEW Import**
import com.example.sixthsenzM5.fragments.ForumFragment;
import com.example.sixthsenzM5.fragments.TrainingFragment;
import com.example.sixthsenzM5.fragments.JobsFragment;
import com.example.sixthsenzM5.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Assuming your layout file is activity_main.xml
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set the listener for item selection on the navigation bar
        bottomNavigationView.setOnItemSelectedListener(this);

        // Load the default fragment (HOME) when the activity is created
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment()); // **Default changed to HomeFragment**
            bottomNavigationView.setSelectedItemId(R.id.nav_home); // **Set selected ID to Home**
        }
    }

    /**
     * Handles the replacement of the current fragment when a bottom navigation item is selected.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();

        // 1. Check for the ID of the selected menu item
        if (itemId == R.id.nav_home) { // **NEW HOME LOGIC**
            selectedFragment = new HomeFragment();
        } else if (itemId == R.id.nav_jobs) {
            selectedFragment = new JobsFragment();
        } else if (itemId == R.id.nav_training) {
            selectedFragment = new TrainingFragment();
        } else if (itemId == R.id.nav_community) {
            selectedFragment = new ForumFragment();
        } else if (itemId == R.id.nav_profile) {
            selectedFragment = new ProfileFragment();
        }

        // 2. Load the determined fragment
        if (selectedFragment != null) {
            loadFragment(selectedFragment);
            return true;
        }

        // If no fragment was loaded (e.g., ID didn't match), return false.
        return false;
    }

    /**
     * Helper method to perform the fragment transaction (swapping)
     */
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                // R.id.fragment_container is the FrameLayout in activity_main.xml
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}