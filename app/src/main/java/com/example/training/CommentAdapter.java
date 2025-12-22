package com.example.training;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final List<Comment> commentList = new ArrayList<>();

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.author.setText(comment.getAuthor());
        holder.content.setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    // ✅ REQUIRED for Room + LiveData
    public void setComments(List<Comment> comments) {
        commentList.clear();
        if (comments != null) {
            commentList.addAll(comments);
        }
        notifyDataSetChanged();
    }


    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView author, content;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.commentAuthor);
            content = itemView.findViewById(R.id.commentContent);
        }
    }
}
