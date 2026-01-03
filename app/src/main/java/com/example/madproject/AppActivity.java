package com.example.madproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.AppDatabase;
import com.example.homepage.MainActivity;
import com.example.jobsearch.Job;

import java.util.ArrayList;
import java.util.List;

public class AppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        seedSampleJobs();
        setContentView(R.layout.activity_app_profile);
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

    private void seedSampleJobs() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

            if (db.jobDao().getCount() == 0) {
                User e1 = new User();
                e1.userType = "employer";
                e1.email = "alice@gmail.com";
                e1.companyName = "Tan Cleaning Services";
                e1.password = "123";
                db.userDao().insert(e1); // capture auto-generated ID

                User e2 = new User();
                e2.userType = "employer";
                e2.email = "bob@gmail.com";
                e2.companyName = "Bobby Construction";
                e2.password = "123";
                db.userDao().insert(e2);

                User e3 = new User();
                e3.userType = "employer";
                e3.email = "catherine@gmail.com";
                e3.companyName = "Homecook Catering";
                e3.password = "123";
                db.userDao().insert(e3);

                User e4 = new User();
                e4.userType = "employee";
                e4.email = "abu@gmail.com";
                e4.fullName = "Abu bin Ali";
                e4.password = "123";
                db.userDao().insert(e4);

                User e5 = new User();
                e5.userType = "employee";
                e5.email = "sam@gmail.com";
                e5.fullName = "Sam Chin";
                e5.password = "123";
                db.userDao().insert(e5);

                User e6 = new User();
                e6.userType = "employee";
                e6.email = "siti@gmail.com";
                e6.fullName = "Siti binti Ahmad";
                e6.password = "123";
                db.userDao().insert(e6);
            }



            if (db.jobDao().getCount() == 0) {
                List<Job> sampleJobs = new ArrayList<>();
                sampleJobs.add(new Job("Construction Helper", "Lee Construction", "Kuala Lumpur",
                        "Construction", "Manual labor", "RM 80/Day", "1.2 km", "95%",2));
                sampleJobs.add(new Job("Home Cleaner", "Tan Cleaning Services", "Petaling Jaya",
                        "Domestic", "Cleaning", "RM 50/Day", "0.5 km", "88%",1));
                sampleJobs.add(new Job("Cook", "Ong Catering", "Subang",
                        "Food Service", "Customer Service", "RM 60/Day", "2.0 km", "80%",3));
                sampleJobs.add(new Job("Gardener", "Tan Cleaning Services", "Kuala Lumpur",
                        "Domestic", "Gardening", "RM 55/Day", "1.5 km", "90%", 1));
                sampleJobs.add(new Job("Plumber Assistant", "Lee Construction", "Petaling Jaya",
                        "Construction", "Plumbing", "RM 85/Day", "2.0 km", "92%", 2));
                sampleJobs.add(new Job("Event Caterer", "Ong Catering", "Kuala Lumpur",
                        "Food Service", "Cooking & Serving", "RM 70/Day", "1.8 km", "85%", 3));
                sampleJobs.add(new Job("Window Cleaner", "Tan Cleaning Services", "Shah Alam",
                        "Domestic", "Cleaning", "RM 45/Day", "3.0 km", "87%", 1));
                sampleJobs.add(new Job("Carpentry Helper", "Lee Construction", "Subang",
                        "Construction", "Carpentry", "RM 90/Day", "2.5 km", "89%", 2));
                sampleJobs.add(new Job("Kitchen Assistant", "Ong Catering", "Petaling Jaya",
                        "Food Service", "Cooking & Prep", "RM 65/Day", "1.0 km", "83%", 3));
                sampleJobs.add(new Job("Laundry Worker", "Tan Cleaning Services", "Kuala Lumpur",
                        "Domestic", "Laundry & Cleaning", "RM 50/Day", "0.8 km", "91%", 1));


                db.jobDao().insertAll(sampleJobs);
            }
        }).start();
    }
}



