package com.example.jobsearchmatching;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class JobAdaptor extends RecyclerView.Adapter<JobAdaptor.JobViewHolder> {

    private List<job_details> jobList;

    // Constructor to receive the list of jobs
    public JobAdaptor(List<job_details> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflates the item_job_card layout you created earlier
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_card, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        // Binds data from your job_details model to the UI components
        job_details job = jobList.get(position);
        holder.tvTitle.setText(job.getTitle());
        holder.tvCategory.setText(job.getCategory());
        holder.tvPay.setText(job.getPayRate());
        holder.tvDist.setText(job.getDistance());
        holder.tvScore.setText(job.getMatchScore());
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    // ViewHolder class to hold references to your item_job_card.xml views
    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvPay, tvDist, tvScore;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvJobTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPay = itemView.findViewById(R.id.tvPayRate);
            tvDist = itemView.findViewById(R.id.tvDistance);
            tvScore = itemView.findViewById(R.id.tvMatchScore);
        }
    }
}