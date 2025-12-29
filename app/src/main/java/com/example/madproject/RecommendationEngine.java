package com.example.madproject;

import com.example.jobsearch.Job;

import java.util.ArrayList;
import java.util.List;

public class RecommendationEngine {

    public static List<Job> getRecommendedJobs(User user, List<Job> jobs) {

        List<Job> result = new ArrayList<>();

        for (Job job : jobs) {

            boolean locationMatch =
                    job.location != null &&
                            user.location != null &&
                            job.location.equalsIgnoreCase(user.location);

            boolean skillMatch =
                    job.skills != null &&
                            user.skills != null &&
                            user.skills.toLowerCase().contains(job.skills.toLowerCase());

            boolean industryMatch =
                    job.industry != null &&
                            user.industry != null &&
                            job.industry.equalsIgnoreCase(user.industry);

            if (locationMatch || skillMatch || industryMatch) {
                result.add(job);
            }
        }
        return result;
    }
}