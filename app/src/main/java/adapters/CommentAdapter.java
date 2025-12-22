package com.example.sixthsenzM5.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sixthsenzM5.R;
import com.example.sixthsenzM5.models.Comment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final Context context;
    private final List<Comment> commentList;
    // Format to display the comment time
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a, MMM dd", Locale.getDefault());

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    // --- 1. Creates a new ViewHolder instance ---
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ASSUMPTION: The layout file for a single comment item is named item_comment.xml
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    // --- 2. Binds data to the view components ---
    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment currentComment = commentList.get(position);

        // Bind data from the Comment object
        holder.usernameTextView.setText(currentComment.getUsername());
        holder.commentTextView.setText(currentComment.getText());

        // Format and display the timestamp
        String formattedDate = dateFormat.format(new Date(currentComment.getTimestamp()));
        holder.timestampTextView.setText(formattedDate);

        // Note: Unlike the main post, comments typically don't need a click listener
        // unless you plan to implement reply functionality.
    }

    // --- 3. Returns the total number of comments in the list ---
    @Override
    public int getItemCount() {
        return commentList.size();
    }

    // --- ViewHolder: Caches the view components ---
    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView usernameTextView, commentTextView, timestampTextView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            // ASSUMPTION: These IDs are defined in item_comment.xml
            usernameTextView = itemView.findViewById(R.id.text_comment_username);
            commentTextView = itemView.findViewById(R.id.text_comment_text);
            timestampTextView = itemView.findViewById(R.id.text_comment_timestamp);
        }
    }
}
