package com.example.jobposting;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.jobsearch.Job;
import com.example.madproject.R;
import com.example.AppDatabase;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddJobActivity extends AppCompatActivity {

    EditText etTitle, etCompany, etLocation, etIndustry, etSkills, etPayRate, etDescription;
    Button btnSave;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        etTitle = findViewById(R.id.etTitle);
        etCompany = findViewById(R.id.etCompany);
        etLocation = findViewById(R.id.etLocation);
        etIndustry = findViewById(R.id.etIndustry);
        etSkills = findViewById(R.id.etSkills);
        etPayRate = findViewById(R.id.etPayRate);
        etDescription = findViewById(R.id.etDescription);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            int employerId = prefs.getInt("user_id", -1);
            String employerAddress = prefs.getString("employer_location", ""); // system-stored

            String jobTitle = etTitle.getText().toString();
            String jobCompany = etCompany.getText().toString();
            String jobLocation = etLocation.getText().toString();
            String description = etDescription.getText().toString();
            String jobIndustry = etIndustry.getText().toString();
            String jobSkills = etSkills.getText().toString();
            String jobPayRate = etPayRate.getText().toString();

            // Run geocoding and database insert in background
            executor.execute(() -> {
                double distanceKm = calculateDistance(employerAddress, jobLocation);

                Job job = new Job(
                        jobTitle,
                        jobCompany,
                        jobLocation,
                        jobIndustry,
                        jobSkills,
                        description,
                        jobPayRate,
                        distanceKm,   // distance in km
                        0,          // matchScore (system will calculate later)
                        employerId
                );

                AppDatabase.getDatabase(this).jobDao().insert(job);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Job posted successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });
    }

    // ============================================================
    // Geocode two addresses and calculate straight-line distance
    // ============================================================
    private double calculateDistance(String address1, String address2) {
        LatLng loc1 = geocodeAddress(address1);
        LatLng loc2 = geocodeAddress(address2);

        if (loc1 == null || loc2 == null) return 0;

        Location l1 = new Location("");
        l1.setLatitude(loc1.latitude);
        l1.setLongitude(loc1.longitude);

        Location l2 = new Location("");
        l2.setLatitude(loc2.latitude);
        l2.setLongitude(loc2.longitude);

        float distanceMeters = l1.distanceTo(l2);
        return distanceMeters / 1000.0; // return km
    }

    // Geocode a string address into LatLng
    private LatLng geocodeAddress(String addressStr) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(addressStr, 1);
            if (addresses == null || addresses.isEmpty()) return null;

            Address address = addresses.get(0);
            return new LatLng(address.getLatitude(), address.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Simple LatLng class (if you don’t want Google Maps dependency)
    private static class LatLng {
        double latitude;
        double longitude;
        LatLng(double lat, double lon) {
            latitude = lat;
            longitude = lon;
        }
    }

    // Handle back button click
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
