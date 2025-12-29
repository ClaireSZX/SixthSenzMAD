package com.example.madproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jobsearch.Job;

import java.util.List;

public class ItemRecommendation extends RecyclerView.Adapter<ItemRecommendation.ViewHolder> {

    List<Job> jobs;

    public ItemRecommendation(List<Job> jobs) {
        this.jobs = jobs;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, company, location;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.jobTitle);
            company = itemView.findViewById(R.id.jobCompany);
            location = itemView.findViewById(R.id.jobLocation);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recommendation, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Job job = jobs.get(position);
        holder.title.setText(job.title);
        holder.company.setText(job.company);
        holder.location.setText(job.location);
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }
}