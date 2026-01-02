package com.example.jobsearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;

import java.util.ArrayList;
import java.util.List;

public class JobAdaptor extends RecyclerView.Adapter<JobAdaptor.JobViewHolder>
        implements Filterable {

    public interface OnJobClickListener {
        void onJobClick(Job job);
    }

    private List<Job> jobList;
    private List<Job> jobListFull;
    private final OnJobClickListener listener;

    public JobAdaptor(List<Job> jobList, OnJobClickListener listener) {
        this.jobList = jobList;
        this.jobListFull = new ArrayList<>(jobList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_job_card, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);

        holder.tvJobTitle.setText(job.getTitle());
        holder.tvCompany.setText(job.getCompany());
        holder.tvCategory.setText(job.getIndustry());
        holder.tvPayRate.setText(job.getPayRate());
        holder.tvDistance.setText(job.getDistance());
        holder.tvMatchScore.setText(job.getMatchScore());

        holder.itemView.setOnClickListener(v -> listener.onJobClick(job));
    }

    @Override
    public int getItemCount() {
        return jobList == null ? 0 : jobList.size();
    }

    // ---------------- FILTER ----------------
    @Override
    public Filter getFilter() {
        return jobFilter;
    }

    private final Filter jobFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Job> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(jobListFull);
            } else {
                String filter = constraint.toString().toLowerCase().trim();

                for (Job job : jobListFull) {
                    if (
                            job.getTitle().toLowerCase().contains(filter) ||
                                    job.getCompany().toLowerCase().contains(filter) ||
                                    job.getIndustry().toLowerCase().contains(filter)
                    ) {
                        filteredList.add(job);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            jobList.clear();
            jobList.addAll((List<Job>) results.values);
            notifyDataSetChanged();
        }
    };

    static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvCategory, tvCompany, tvPayRate, tvDistance, tvMatchScore;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvCompany = itemView.findViewById(R.id.tvCompany);
            tvPayRate = itemView.findViewById(R.id.tvPayRate);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvMatchScore = itemView.findViewById(R.id.tvMatchScore);
        }
    }
}
