package com.example.training;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ForumPostAdapter extends RecyclerView.Adapter<ForumPostAdapter.PostViewHolder> {

    private List<ForumPost> postList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ForumPost post);
    }

    public ForumPostAdapter(List<ForumPost> postList, OnItemClickListener listener) {
        this.postList = postList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forum_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        ForumPost post = postList.get(position);
        holder.username.setText(post.getAuthor());
        holder.content.setText(post.getContent());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        holder.timestamp.setText(sdf.format(new Date(post.getTimestamp())));

        holder.commentCount.setText("View Comments (" + post.getCommentCount() + ")");
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(post);
        });
    }

    @Override
    public int getItemCount() { return postList.size(); }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView username, content, timestamp, commentCount;

        PostViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.postUsername);
            content = itemView.findViewById(R.id.postContent);
            timestamp = itemView.findViewById(R.id.postTimestamp);
            commentCount = itemView.findViewById(R.id.commentCount);
        }
    }
}
