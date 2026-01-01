package com.example.jobsearch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;

import java.util.ArrayList;
import java.util.List;

public class JobAdaptor extends RecyclerView.Adapter<JobAdaptor.JobViewHolder> implements Filterable {

    public interface OnJobClickListener {
        void onJobClick(Job job);
    }

    private List<Job> jobList;
    private List<Job> jobListFull; // full copy for filtering
    private OnJobClickListener listener;

    // Constructor
    public JobAdaptor(List<Job> jobList, OnJobClickListener listener) {
        this.jobList = jobList;
        this.listener = listener;
        this.jobListFull = new ArrayList<>(jobList); // copy for search/filter
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_card, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);

        holder.tvJobTitle.setText(job.title);
        holder.tvCategory.setText(job.industry); // use correct field
        holder.tvPayRate.setText(job.payRate);
        holder.tvDistance.setText(job.distance);
        holder.tvMatchScore.setText(job.matchScore);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onJobClick(job);
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    // ✅ Update jobs (adapter method, not in Filter)
    public void updateJobs(List<Job> newJobs) {
        jobList.clear();
        jobList.addAll(newJobs);
        jobListFull.clear();
        jobListFull.addAll(newJobs); // keep full copy for filtering
        notifyDataSetChanged();
    }

    // Filterable
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
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Job job : jobListFull) {
                    if (job.title.toLowerCase().contains(filterPattern) ||
                            job.industry.toLowerCase().contains(filterPattern)) {
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

    // ViewHolder
    static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvCategory, tvPayRate, tvDistance, tvMatchScore;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPayRate = itemView.findViewById(R.id.tvPayRate);
            tvDistance = itemView.findViewById(R.id.tvDistance);
            tvMatchScore = itemView.findViewById(R.id.tvMatchScore);
        }
    }
}
