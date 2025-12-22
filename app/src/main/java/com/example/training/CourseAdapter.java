package com.example.training;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> courseList;
    private Context context;

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
        Course course = courseList.get(position);
        holder.title.setText(course.getTitle());
        holder.category.setText(course.getCategory());
        holder.duration.setText(course.getDuration());

        // Card click opens details
        holder.courseCard.setOnClickListener(v -> {
            Intent intent = new Intent(context, ActivityCourseDetail.class);
            intent.putExtra("course_id", course.getId());
            intent.putExtra("title", course.getTitle());
            intent.putExtra("content_url", course.getContentUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        CardView courseCard;
        TextView title, category, duration;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseCard = itemView.findViewById(R.id.courseCard);
            title = itemView.findViewById(R.id.courseTitle);
            category = itemView.findViewById(R.id.courseCategory);
            duration = itemView.findViewById(R.id.courseDuration);
        }
    }
}
