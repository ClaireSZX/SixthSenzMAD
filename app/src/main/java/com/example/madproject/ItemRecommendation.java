
package com.example.madproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jobsearch.Job;
import com.example.madproject.R;

import java.util.List;

public class ItemRecommendation extends RecyclerView.Adapter<ItemRecommendation.ViewHolder> {

    private List<Job> jobs;
    private OnJobClickListener listener;

    // Click listener interface
    public interface OnJobClickListener {
        void onJobClick(Job job);
    }

    // Updated constructor with click listener
    public ItemRecommendation(List<Job> jobs, OnJobClickListener listener) {
        this.jobs = jobs;
        this.listener = listener;
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

        // Set click listener for each item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onJobClick(job);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }
}
