package com.example.homepage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jobsearch.Job;
import com.example.madproject.R;

import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder> {

    private List<Job> companies;

    public CompanyAdapter(List<Job> companies) {
        this.companies = companies;
    }

    @Override
    public CompanyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recommendation_company, parent, false); // simple layout with TextView for companyName
        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CompanyViewHolder holder, int position) {
        Job job = companies.get(position);
        holder.TV_userName.setText(job.getCompany());
    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    public static class CompanyViewHolder extends RecyclerView.ViewHolder {
        TextView TV_userName;

        public CompanyViewHolder(View itemView) {
            super(itemView);
            TV_userName = itemView.findViewById(R.id.TV_userName);
        }
    }
}

