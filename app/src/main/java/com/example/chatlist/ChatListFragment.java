package com.example.chatlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;

import java.util.ArrayList;
import java.util.List;

public class ChatListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatListAdapter chatListAdapter;
    private List<ChatItem> sampleChatList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_chats);

        sampleChatList = createSampleData();

        chatListAdapter = new ChatListAdapter(requireContext(), sampleChatList, item -> {
            Intent intent = new Intent(requireContext(), ChatBox.class);
            intent.putExtra("PARTNER_NAME", item.getUserName());
            intent.putExtra("PARTNER_ID", item.getUserId());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(chatListAdapter);

        ImageButton adminButton = view.findViewById(R.id.IB_adminButton);

        adminButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), adminChat.class);
            startActivity(intent);
        });


        return view;
    }



    private List<ChatItem> createSampleData() {
        List<ChatItem> list = new ArrayList<>();
        list.add(new ChatItem("u001", "Alice Chin", "I'm interested in the Senior Marketing...", "9:30 PM"));
        list.add(new ChatItem("u002", "Jane Smith", "Thanks.", "8:45 PM"));
        list.add(new ChatItem("u003", "Charlie Watson", "I’ll reach out once we have a decision.", "Yesterday"));
        list.add(new ChatItem("u004", "Ivan Lim", "", "11/25/2025"));
        return list;
    }
}
