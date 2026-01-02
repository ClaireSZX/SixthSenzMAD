package com.example.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AppDatabase;
import com.example.jobsearch.Job;
import com.example.madproject.ItemRecommendation;
import com.example.jobsearch.JobDetailActivity;
import com.example.madproject.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EmployerHomeFragment extends Fragment {

    private RecyclerView recyclerJobs, recyclerCompanies;
    private TextView emptyJobsText, emptyCompaniesText;
    private ItemRecommendation jobAdapter;
    private CompanyAdapter companyAdapter;
    private List<Job> jobList = new ArrayList<>();
    private List<Job> companyList = new ArrayList<>(); // use Job objects for companies

    public EmployerHomeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.employer_fragment_home, container, false);

        // --- RecyclerViews and Empty TextViews ---
        emptyJobsText = view.findViewById(R.id.textEmpty);
        recyclerJobs = view.findViewById(R.id.recyclerJobs);

        emptyCompaniesText = view.findViewById(R.id.textEmpty2);
        recyclerCompanies = view.findViewById(R.id.recyclerCompanies);

        // --- LayoutManagers ---
        recyclerJobs.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        recyclerCompanies.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        // --- Adapters ---
        jobAdapter = new ItemRecommendation(jobList, job -> {
            Intent intent = new Intent(getActivity(), JobDetailActivity.class);

            // Pass all job fields exactly as your JobDetailActivity expects
            intent.putExtra(JobDetailActivity.EXTRA_TITLE, job.title);
            intent.putExtra(JobDetailActivity.EXTRA_COMPANY, job.company);
            intent.putExtra(JobDetailActivity.EXTRA_CATEGORY, job.industry);
            intent.putExtra(JobDetailActivity.EXTRA_PAY, job.payRate);
            intent.putExtra(JobDetailActivity.EXTRA_DISTANCE, job.distance);
            intent.putExtra(JobDetailActivity.EXTRA_SCORE, job.matchScore);

            startActivity(intent);
        });
        recyclerJobs.setAdapter(jobAdapter);

        companyAdapter = new CompanyAdapter(companyList);
        recyclerCompanies.setAdapter(companyAdapter);

        // --- Load Data ---
        loadJobsFromDatabase();
        loadCompaniesFromJobs();

        // --- Button to Job Search ---
        Button btnGoToSearch = view.findViewById(R.id.btnGoToSearch);
        btnGoToSearch.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), com.example.jobsearch.JobSearchActivity.class);
            startActivity(intent);
        });

        return view;
    }

    // --- Load Jobs ---
    private void loadJobsFromDatabase() {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        new Thread(() -> {
            List<Job> dbJobs = db.jobDao().getAllJobs();
            getActivity().runOnUiThread(() -> {
                jobList.clear();
                jobList.addAll(dbJobs);
                jobAdapter.notifyDataSetChanged();

                emptyJobsText.setVisibility(jobList.isEmpty() ? View.VISIBLE : View.GONE);
                recyclerJobs.setVisibility(jobList.isEmpty() ? View.GONE : View.VISIBLE);
            });
        }).start();
    }

    // --- Load Companies from Jobs ---
    private void loadCompaniesFromJobs() {
        AppDatabase db = AppDatabase.getDatabase(getContext());
        new Thread(() -> {
            List<Job> dbJobs = db.jobDao().getAllJobs();
            Set<String> seenCompanies = new HashSet<>();
            List<Job> uniqueCompanies = new ArrayList<>();

            for (Job job : dbJobs) {
                String companyName = job.getCompany(); // must exist in Job class
                if (!seenCompanies.contains(companyName)) {
                    seenCompanies.add(companyName);
                    uniqueCompanies.add(job); // keep one job per company for display
                }
            }

            getActivity().runOnUiThread(() -> {
                companyList.clear();
                companyList.addAll(uniqueCompanies);
                companyAdapter.notifyDataSetChanged();

                emptyCompaniesText.setVisibility(companyList.isEmpty() ? View.VISIBLE : View.GONE);
                recyclerCompanies.setVisibility(companyList.isEmpty() ? View.GONE : View.VISIBLE);
            });
        }).start();
    }
}
