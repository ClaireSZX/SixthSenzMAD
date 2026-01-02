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
import com.example.madproject.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobAdaptor extends RecyclerView.Adapter<JobAdaptor.JobViewHolder>
        implements Filterable {

    public interface OnJobClickListener {
        void onJobClick(Job job);
    }

    private List<Job> jobList;
    private List<Job> jobListFull;
    private OnJobClickListener listener;

    // employerId -> companyName
    private final Map<Integer, String> employerCompanyMap = new HashMap<>();

    // Constructor
    public JobAdaptor(List<Job> jobList, List<User> users, OnJobClickListener listener) {
        this.jobList = jobList;
        this.jobListFull = new ArrayList<>(jobList);
        this.listener = listener;

        // Build employer map
        for (User user : users) {
            employerCompanyMap.put(user.id, user.companyName);
        }
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

        holder.tvJobTitle.setText(job.title);
        holder.tvCategory.setText(job.industry);
        holder.tvCompany.setText(job.company);
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

    // ================= FILTER LOGIC =================
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

                    String companyName =
                            employerCompanyMap.getOrDefault(job.employerId, "");

                    boolean matches =
                            job.title.toLowerCase().contains(filterPattern) ||
                                    job.industry.toLowerCase().contains(filterPattern) ||
                                    job.location.toLowerCase().contains(filterPattern) ||
                                    companyName.toLowerCase().contains(filterPattern);

                    if (matches) {
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

    // ================= VIEW HOLDER =================
    static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvCategory, tvPayRate, tvDistance, tvMatchScore,tvCompany;

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
