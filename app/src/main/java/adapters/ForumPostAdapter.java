package com.example.sixthsenzM5.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sixthsenzM5.R;
import com.example.sixthsenzM5.activities.PostDetailActivity;
import com.example.sixthsenzM5.models.ForumPost;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ForumPostAdapter extends RecyclerView.Adapter<ForumPostAdapter.PostViewHolder> {

    private final Context context;
    private final List<ForumPost> postList;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.getDefault());

    public ForumPostAdapter(Context context, List<ForumPost> postList) {
        this.context = context;
        this.postList = postList;
    }

    // --- 1. Creates a new ViewHolder instance ---
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflates the layout for a single forum post item
        View view = LayoutInflater.from(context).inflate(R.layout.item_forum_post, parent, false);
        return new PostViewHolder(view);
    }

    // --- 2. Binds data to the view components ---
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        ForumPost currentPost = postList.get(position);

        // Bind data from the ForumPost object to the TextViews
        holder.usernameTextView.setText(currentPost.getUsername());
        holder.contentTextView.setText(currentPost.getContent());

        // Format the long timestamp into a readable date string
        String formattedDate = dateFormat.format(new Date(currentPost.getTimestamp()));
        holder.timestampTextView.setText(formattedDate);

        // Set click listener to open the detailed view (PostDetailActivity)
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailActivity.class);

            // Pass the unique Post ID so the detail activity can fetch comments
            intent.putExtra("POST_ID", currentPost.getPostID());

            // Optionally, pass the main post content to display instantly
            intent.putExtra("POST_CONTENT", currentPost.getContent());
            intent.putExtra("POST_USERNAME", currentPost.getUsername());

            context.startActivity(intent);
        });
    }

    // --- 3. Returns the total number of posts in the list ---
    @Override
    public int getItemCount() {
        return postList.size();
    }

    // --- ViewHolder: Caches the view components for better performance ---
    public static class PostViewHolder extends RecyclerView.ViewHolder {

        TextView usernameTextView, contentTextView, timestampTextView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            // Map the Java variables to the IDs defined in item_forum_post.xml
            usernameTextView = itemView.findViewById(R.id.text_post_username);
            contentTextView = itemView.findViewById(R.id.text_post_content);
            timestampTextView = itemView.findViewById(R.id.text_post_timestamp);

            // Note: You may also add views for a comment counter or upvotes here
        }
    }
}