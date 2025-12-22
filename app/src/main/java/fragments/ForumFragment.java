package com.example.sixthsenzM5.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment; // ⬅️ CORRECT BASE CLASS
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sixthsenzM5.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.example.sixthsenzM5.models.ForumPost;
import com.example.sixthsenzM5.adapters.ForumPostAdapter;
import com.example.sixthsenzM5.activities.NewPostActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ForumFragment extends Fragment {

    private static final String TAG = "ForumFragment";
    private RecyclerView recyclerView;
    private ForumPostAdapter adapter;
    private List<ForumPost> postList;
    private FirebaseFirestore db;
    private ListenerRegistration firestoreListener;

    /**
     * 1. Inflates the layout for this fragment.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // ASSUMPTION: Layout file is fragment_forum.xml (renamed from activity_forum.xml)
        return inflater.inflate(R.layout.fragment_forum, container, false);
    }

    /**
     * 2. Initializes views, adapters, and starts the database listener.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Context safely
        Context context = getContext();
        if (context == null) return;

        // Initialization: Use view.findViewById()
        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recycler_view_forum_posts);
        postList = new ArrayList<>();

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        // Use context for the adapter
        adapter = new ForumPostAdapter(context, postList);
        recyclerView.setAdapter(adapter);

        // Start listening for real-time updates
        startRealtimeListener(context);

        // Inside ForumFragment.java -> onViewCreated()
        FloatingActionButton fabAddPost = view.findViewById(R.id.fab_add_post);
        fabAddPost.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), NewPostActivity.class);
            startActivity(intent);
        });
    }

    private void startRealtimeListener(Context context) {
        firestoreListener = db.collection("posts")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        if (isAdded()) {
                            Toast.makeText(context, "Failed to load community feed.", Toast.LENGTH_LONG).show();
                        }
                        return;
                    }

                    if (snapshots != null) {
                        if (isAdded()) {
                            postList.clear();
                            for (DocumentSnapshot doc : snapshots.getDocuments()) {
                                ForumPost post = ForumPost.fromMap(doc.getId(), doc.getData());
                                if (post != null) postList.add(post);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.d(TAG, "No posts found in Firestore.");
                    }
                });
    }

    /**
     * 3. Lifecycle cleanup: Stop the listener when the view is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Crucial: Stop the listener to prevent memory leaks when the fragment view is swapped out
        if (firestoreListener != null) {
            firestoreListener.remove();
        }
        // NOTE: onDestroyView is used instead of onDestroy for cleanup related to the Fragment's UI.
    }
}