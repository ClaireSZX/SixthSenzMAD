package com.example.madproject;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.AppDatabase;
import com.example.homepage.MainActivity;
import com.example.jobsearch.Job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppActivity extends AppCompatActivity {

    private static final String DEFAULT_LOCATION = "Kuala Lumpur";

    // Map string locations to approximate lat/lng
    private static final Map<String, double[]> locationCoordinates = new HashMap<>();
    static {
        locationCoordinates.put("Kuala Lumpur", new double[]{3.1390, 101.6869});
        locationCoordinates.put("Petaling Jaya", new double[]{3.1073, 101.6067});
        locationCoordinates.put("Subang", new double[]{3.0683, 101.5500});
        locationCoordinates.put("Shah Alam", new double[]{3.0738, 101.5183});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_profile);

        seedSampleData();
    }

    // -------------------------------
    // Handle signup result
    // -------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            int userId = data.getIntExtra("userId", -1);
            if (userId != -1) {
                startActivity(new android.content.Intent(this, MainActivity.class)
                        .putExtra("userId", userId));
                finish();
            }
        }
    }

    // -------------------------------
    // Seed sample users and jobs
    // -------------------------------
    private void seedSampleData() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String userLocation = prefs.getString("user_location", DEFAULT_LOCATION);
            String userSkills = prefs.getString("user_skills", ""); // optional, for match score

            // 1️⃣ Seed users
            if (db.userDao().getCount() == 0) {
                User e1 = new User(); e1.userType = "employer"; e1.email = "alice@gmail.com"; e1.companyName = "Tan Cleaning Services"; e1.password = "123"; db.userDao().insert(e1);
                User e2 = new User(); e2.userType = "employer"; e2.email = "bob@gmail.com"; e2.companyName = "Bobby Construction"; e2.password = "123"; db.userDao().insert(e2);
                User e3 = new User(); e3.userType = "employer"; e3.email = "catherine@gmail.com"; e3.companyName = "Homecook Catering"; e3.password = "123"; db.userDao().insert(e3);
                User e4 = new User(); e4.userType = "employee"; e4.email = "abu@gmail.com"; e4.fullName = "Abu bin Ali"; e4.password = "123";e4.location = "Subang Jaya"; db.userDao().insert(e4);
                User e5 = new User(); e5.userType = "employee"; e5.email = "sam@gmail.com"; e5.fullName = "Sam Chin"; e5.password = "123"; e5.location = "Petaling Jaya";db.userDao().insert(e5);
                User e6 = new User(); e6.userType = "employee"; e6.email = "siti@gmail.com"; e6.fullName = "Siti binti Ahmad"; e6.password = "123";e6.location = "Kuala Lumpur"; db.userDao().insert(e6);
            }

            // 2️⃣ Seed jobs
            if (db.jobDao().getCount() == 0) {
                List<Job> sampleJobs = new ArrayList<>();

                sampleJobs.add(new Job("Construction Helper", "Lee Construction", "Jalan Tun Razak, Kuala Lumpur",
                        "Construction", "Manual labor", "Assist with basic construction tasks, lifting, and site preparation",
                        "RM 80/Day", 0, 0, 2));

                sampleJobs.add(new Job("Home Cleaner", "Tan Cleaning Services", "Jalan SS2/72, Petaling Jaya",
                        "Domestic", "Cleaning", "Perform general home cleaning, dusting, and floor maintenance",
                        "RM 50/Day", 0, 0, 1));

                sampleJobs.add(new Job("Cook", "Ong Catering", "Jalan USJ 1/1, Subang Jaya",
                        "Food Service", "Customer Service", "Prepare meals according to recipes, maintain kitchen hygiene",
                        "RM 60/Day", 0, 0, 3));

                sampleJobs.add(new Job("Gardener", "Tan Cleaning Services", "Bukit Tunku, Kuala Lumpur",
                        "Domestic", "Gardening", "Plant, water, and maintain gardens and outdoor plants",
                        "RM 55/Day", 0, 0, 1));

                sampleJobs.add(new Job("Plumber Assistant", "Lee Construction", "Jalan SS5/8, Petaling Jaya",
                        "Construction", "Plumbing", "Assist in plumbing installations and repairs under supervision",
                        "RM 85/Day", 0, 0, 2));

                sampleJobs.add(new Job("Event Caterer", "Ong Catering", "Jalan Raja Chulan, Kuala Lumpur",
                        "Food Service", "Cooking & Serving", "Help with food preparation, serving guests, and cleaning up",
                        "RM 70/Day", 0, 0, 3));

                sampleJobs.add(new Job("Window Cleaner", "Tan Cleaning Services", "Seksyen 7, Shah Alam",
                        "Domestic", "Cleaning", "Clean windows and glass surfaces efficiently and safely",
                        "RM 45/Day", 0, 0, 1));

                sampleJobs.add(new Job("Carpentry Helper", "Lee Construction", "Jalan USJ 10/1, Subang Jaya",
                        "Construction", "Carpentry", "Assist carpenters in cutting, assembling, and finishing wood projects",
                        "RM 90/Day", 0, 0, 2));

                sampleJobs.add(new Job("Kitchen Assistant", "Ong Catering", "Jalan SS2/6, Petaling Jaya",
                        "Food Service", "Cooking & Prep", "Support chefs in meal prep, chopping ingredients, and cleaning kitchen",
                        "RM 65/Day", 0, 0, 3));

                sampleJobs.add(new Job("Laundry Worker", "Tan Cleaning Services", "Jalan Bukit Bintang, Kuala Lumpur",
                        "Domestic", "Laundry & Cleaning", "Wash, dry, fold, and organize laundry for clients",
                        "RM 50/Day", 0, 0, 1));



                // Calculate distance & match score
                for (Job job : sampleJobs) {
                    double distanceKm = calculateDistance(userLocation, job.getLocation());
                    job.setDistance(distanceKm);

                    int skillScore = calculateSkillMatch(userSkills, job.getSkills());
                    int distanceScore = (int) Math.max(0, 100 - distanceKm * 10); // closer = higher

                    double matchScore = skillScore * 0.7 + distanceScore * 0.3;
                    job.setMatchScore(matchScore);
                }

                db.jobDao().insertAll(sampleJobs);
            }
        }).start();
    }

    // -------------------------------
    // Skill match calculation (0-100)
    // -------------------------------
    private int calculateSkillMatch(String userSkills, String jobSkills) {
        if (userSkills == null || userSkills.isEmpty() || jobSkills == null || jobSkills.isEmpty()) return 0;

        String[] userArr = userSkills.split(",");
        String[] jobArr = jobSkills.split(",");

        int matches = 0;
        for (String skill : jobArr) {
            for (String uSkill : userArr) {
                if (uSkill.trim().equalsIgnoreCase(skill.trim())) {
                    matches++;
                    break;
                }
            }
        }
        return (int) ((matches / (double) jobArr.length) * 100);
    }

    // -------------------------------
    // Straight-line distance calculation
    // -------------------------------
    private double calculateDistance(String fromLocation, String toLocation) {
        double[] from = locationCoordinates.getOrDefault(fromLocation, locationCoordinates.get(DEFAULT_LOCATION));
        double[] to = locationCoordinates.getOrDefault(toLocation, locationCoordinates.get(DEFAULT_LOCATION));

        double lat1 = Math.toRadians(from[0]);
        double lon1 = Math.toRadians(from[1]);
        double lat2 = Math.toRadians(to[0]);
        double lon2 = Math.toRadians(to[1]);

        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        double a = Math.sin(dlat / 2) * Math.sin(dlat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dlon / 2) * Math.sin(dlon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double earthRadiusKm = 6371;
        return earthRadiusKm * c; // km
    }
}


