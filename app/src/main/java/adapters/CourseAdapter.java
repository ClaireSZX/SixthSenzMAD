package com.example.sixthsenzM5.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.sixthsenzM5.R;
import com.example.sixthsenzM5.activities.CourseDetailsActivity;
import com.example.sixthsenzM5.models.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private final Context context;
    private final List<Course> courseList;

    public CourseAdapter(Context context, List<Course> courseList) {
        this.context = context;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course_card, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course currentCourse = courseList.get(position);

        holder.title.setText(currentCourse.getTitle());
        holder.category.setText("Category: " + currentCourse.getCategory());
        holder.duration.setText("Duration: " + currentCourse.getDuration());

        // Load image correctly based on whether it's a local drawable or URL
        if (currentCourse.getThumbnailResID() != -1) {
            // Local drawable
            Glide.with(context)
                    .load(currentCourse.getThumbnailResID())
                    .placeholder(R.drawable.ic_course_placeholder)
                    .error(R.drawable.ic_course_placeholder)
                    .into(holder.thumbnail);
        } else if (currentCourse.getThumbnailUrl() != null) {
            // Remote URL
            Glide.with(context)
                    .load(currentCourse.getThumbnailUrl())
                    .placeholder(R.drawable.ic_course_placeholder)
                    .error(R.drawable.ic_course_placeholder)
                    .into(holder.thumbnail);
        }

        // Click to open detail page
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CourseDetailsActivity.class);
            intent.putExtra("COURSE_TITLE", currentCourse.getTitle());
            intent.putExtra("CONTENT_URL", currentCourse.getContentUrl()); // make sure this matches your model
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView title, category, duration;
        ImageView thumbnail;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_course_title);
            category = itemView.findViewById(R.id.text_course_category);
            duration = itemView.findViewById(R.id.text_course_duration);
            thumbnail = itemView.findViewById(R.id.image_thumbnail);
        }
    }
}
