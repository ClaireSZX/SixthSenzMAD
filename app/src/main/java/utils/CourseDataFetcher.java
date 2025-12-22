package com.example.sixthsenzM5.utils;

import com.example.sixthsenzM5.R;
import com.example.sixthsenzM5.models.Course;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class CourseDataFetcher {

    public interface CourseFetchCallback {
        void onCoursesFetched(List<Course> courses);
        void onFailure(Exception e);
    }
    /**
     * Provides a list of mock (sample) Course objects for UI testing and development.
     * This method now uses local drawable resources for thumbnails.
     *
     * @return A List of Course objects.
     */

    public static void fetchCoursesFromFirestore(CourseFetchCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Course> courses = new ArrayList<>();

        db.collection("courses") // Assuming your courses collection is named "courses"
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            // Firebase maps the document fields directly to the Course model
                            Course course = document.toObject(Course.class);
                            if (course != null) {
                                // Optionally set the document ID as the courseId
                                course.setCourseID(document.getId());
                                courses.add(course);
                            }
                        }
                        // Success: Send the list back via the callback
                        callback.onCoursesFetched(courses);
                    } else {
                        // Failure: Handle the error
                        callback.onFailure(task.getException());
                    }
                });
    }
    public static List<Course> getMockCourses() {
        List<Course> mockList = new ArrayList<>();

        // 1. Mock Course: Interview Skills
        mockList.add(new Course(
                "C001",
                "Acing the Local Job Interview",
                "Learn basic communication skills and common interview questions for local jobs.",
                "Job Readiness",
                "10 min",
                R.drawable.interview_thumbnail,   // Local drawable instead of URL
                "https://www.youtube.com/watch?v=mock_video1"
        ));

        // 2. Mock Course: Workplace Safety
        mockList.add(new Course(
                "C002",
                "Basic On-Site Safety and Hazards",
                "Essential safety tips for construction, domestic, and farm work environments.",
                "Safety & Health",
                "8 min",
                R.drawable.safety_thumbnail,
                "https://joblinktraining.org/safety-guide"
        ));

        // 3. Mock Course: Digital Literacy
        mockList.add(new Course(
                "C003",
                "Setting Up a Professional Email",
                "A step-by-step guide to creating and checking an email account for job alerts.",
                "Digital Skills",
                "15 min",
                R.drawable.email_thumbnail,
                "https://www.youtube.com/watch?v=mock_video3"
        ));

        return mockList;
    }

}
