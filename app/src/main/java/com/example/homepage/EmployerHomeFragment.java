package com.example.homepage;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AppDatabase;
import com.example.jobsearch.Job;
import com.example.jobsearch.JobDetailActivity;
import com.example.madproject.ItemRecommendation;
import com.example.madproject.R;
import com.example.madproject.User;

import java.util.ArrayList;
import java.util.List;

public class EmployerHomeFragment extends Fragment {

    private RecyclerView recyclerJobs, recyclerEmployees;
    private TextView emptyJobsText, emptyEmployeesText;

    private ItemRecommendation jobAdapter;
    private EmployeeAdapter employeeAdapter;

    private final List<Job> jobList = new ArrayList<>();
    private final List<User> employeeList = new ArrayList<>();

    private int employerId = -1;

    public EmployerHomeFragment() {}

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.employer_fragment_home, container, false);

        // ----------------------------
        // Get employer ID
        // ----------------------------
        SharedPreferences prefs =
                requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE);

        employerId = prefs.getInt("user_id", -1);

        // ----------------------------
        // Views
        // ----------------------------
        emptyJobsText = view.findViewById(R.id.textEmpty);
        recyclerJobs = view.findViewById(R.id.recyclerJobs);

        emptyEmployeesText = view.findViewById(R.id.textEmpty2);
        recyclerEmployees = view.findViewById(R.id.recyclerCompanies);

        recyclerJobs.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        recyclerEmployees.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        // ----------------------------
        // Job Adapter (Employer's jobs)
        // ----------------------------
        jobAdapter = new ItemRecommendation(jobList, job -> {
            Intent intent = new Intent(requireContext(), JobDetailActivity.class);
            intent.putExtra(JobDetailActivity.EXTRA_TITLE, job.getTitle());
            intent.putExtra(JobDetailActivity.EXTRA_COMPANY, job.getCompany());
            intent.putExtra(JobDetailActivity.EXTRA_CATEGORY, job.getIndustry());
            intent.putExtra(JobDetailActivity.EXTRA_PAY, job.getPayRate());
            intent.putExtra(JobDetailActivity.EXTRA_DISTANCE, job.getDistance());
            intent.putExtra(JobDetailActivity.EXTRA_SCORE, job.getMatchScore());
            intent.putExtra("job_id", job.id);
            startActivity(intent);
        });

        recyclerJobs.setAdapter(jobAdapter);

        // ----------------------------
        // Employee Adapter (Potential employees)
        // ----------------------------
        employeeAdapter = new EmployeeAdapter(employeeList, employee -> {
            // Intentionally empty for now
            // Hook this to profile / chat later
        });

        recyclerEmployees.setAdapter(employeeAdapter);

        // ----------------------------
        // Load data
        // ----------------------------
        loadEmployerJobs();
        loadEmployees();

        return view;
    }

    // ============================================================
    // Jobs posted by THIS employer
    // ============================================================
    private void loadEmployerJobs() {
        AppDatabase db = AppDatabase.getDatabase(requireContext());

        new Thread(() -> {
            // 1️⃣ Get all jobs
            List<Job> allJobs = db.jobDao().getAllJobs();

            // 2️⃣ Filter jobs posted by this employer
            List<Job> employerJobs = new ArrayList<>();
            for (Job job : allJobs) {
                if (job.getEmployerId() == employerId) {
                    employerJobs.add(job);
                }
            }

            // 3️⃣ Update UI
            if (!isAdded()) return;

            requireActivity().runOnUiThread(() -> {
                jobList.clear();
                jobList.addAll(employerJobs);
                jobAdapter.notifyDataSetChanged();

                emptyJobsText.setVisibility(jobList.isEmpty() ? View.VISIBLE : View.GONE);
                recyclerJobs.setVisibility(jobList.isEmpty() ? View.GONE : View.VISIBLE);
            });
        }).start();
    }


    // ============================================================
    // Potential employees
    // ============================================================
    private void loadEmployees() {
        AppDatabase db = AppDatabase.getDatabase(requireContext());

        new Thread(() -> {
            List<User> users = db.userDao().getAllUsers();
            List<User> employees = new ArrayList<>();

            for (User user : users) {
                if ("employee".equalsIgnoreCase(user.userType))
                { employees.add(user); }
            }

            if (!isAdded()) return;

            requireActivity().runOnUiThread(() -> {
                employeeList.clear();
                employeeList.addAll(employees);
                employeeAdapter.notifyDataSetChanged();

                emptyEmployeesText.setVisibility(
                        employeeList.isEmpty() ? View.VISIBLE : View.GONE
                );
                recyclerEmployees.setVisibility(
                        employeeList.isEmpty() ? View.GONE : View.VISIBLE
                );
            });
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadEmployerJobs(); // reload the jobs from database
    }
}
