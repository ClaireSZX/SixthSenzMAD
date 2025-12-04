package com.example.chatlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {

    private final List<ChatItem> chatList;
    private final OnItemClickListener listener;
    private final Context context;

    public interface OnItemClickListener {
        void onItemClick(ChatItem item);
    }

    public ChatListAdapter(Context context, List<ChatItem> chatList, OnItemClickListener listener) {
        this.context = context;
        this.chatList = chatList;
        this.listener = listener;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public TextView lastMessage;
        public TextView timestamp;
        public TextView unreadCount;

        public ChatViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.tv_user_name);
            lastMessage = itemView.findViewById(R.id.tv_last_message);
            timestamp = itemView.findViewById(R.id.tv_timestamp);
        }

        public void bind(final ChatItem item, final OnItemClickListener listener) {
            userName.setText(item.getUserName());
            lastMessage.setText(item.getLastMessage());
            timestamp.setText(item.getTimestamp());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.activity_chat_item, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bind(chatList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}