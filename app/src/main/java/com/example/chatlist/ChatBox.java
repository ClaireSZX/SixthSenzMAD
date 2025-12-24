package com.example.chatlist;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.madproject.R;

import java.util.ArrayList;
import java.util.List;

public class ChatBox extends AppCompatActivity {

    public static final String EXTRA_PARTNER_NAME = "PARTNER_NAME";
    private EditText messageEditText;
    private Button sendButton;
    private LinearLayout messagesLayout;
    private ScrollView chatScrollView;
    private List<Message> messages = new ArrayList<>();
    private String chatPartnerName = "Default User";
    private String chatPartnerId = null;
    private String myName = "John Doe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String receivedName = extras.getString(EXTRA_PARTNER_NAME);
            if (receivedName != null) {
                chatPartnerName = receivedName;
            }

            chatPartnerId = extras.getString("PARTNER_ID");
        }

        TextView contactNameTextView = findViewById(R.id.TV_contactName);
        messageEditText = findViewById(R.id.messageEditText);
        sendButton = findViewById(R.id.sendButton);
        messagesLayout = findViewById(R.id.messagesLayout);
        chatScrollView = findViewById(R.id.chatScrollView);
        ImageButton backButton = findViewById(R.id.IB_backButton);

        if (contactNameTextView != null) {
            contactNameTextView.setText(chatPartnerName);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(chatPartnerName);
        }

        messages.clear();

        if (chatPartnerId != null) {
            messages.addAll(getMockHistory(chatPartnerId));
        }

        renderAllMessages();
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();

        if (messageText.isEmpty()) {
            return;
        }

        Message newMessage = new Message(messageText, true);
        messages.add(newMessage);
        addMessageBubble(newMessage);

        messageEditText.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(messageEditText.getWindowToken(), 0);

        chatScrollView.post(new Runnable() {
            @Override
            public void run() {
                chatScrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private void renderAllMessages() {
        messagesLayout.removeAllViews();
        for (Message message : messages) {
            addMessageBubble(message);
        }
    }

    private void addMessageBubble(Message messageObject) {
        boolean isMe = messageObject.isMe();
        String content = messageObject.getContent();

        TextView messageTextView = new TextView(this);
        messageTextView.setText(content);
        messageTextView.setTextSize(16);
        messageTextView.setTextColor(isMe ? ContextCompat.getColor(this, android.R.color.white) : ContextCompat.getColor(this, android.R.color.black));
        messageTextView.setPadding(30, 20, 30, 20);

        CardView cardView = new CardView(this);
        cardView.setRadius(40);
        cardView.setCardElevation(4);
        cardView.setContentPadding(0, 0, 0, 0);

        int colorResId = isMe ? R.color.colorPrimary : R.color.colorSecondary;
        cardView.setCardBackgroundColor(ContextCompat.getColor(this, colorResId));

        cardView.addView(messageTextView);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(20, 10, 20, 10);

        if (isMe) {
            params.gravity = android.view.Gravity.END;
        } else {
            params.gravity = android.view.Gravity.START;
        }

        cardView.setLayoutParams(params);
        messagesLayout.addView(cardView);
    }

    private List<Message> getMockHistory(String partnerId) {
        List<Message> history = new ArrayList<>();

        switch (partnerId) {
            case "u001": // Alice Chin
                history.add(new Message("Hi, I saw your JobMatch profile!", false));
                history.add(new Message("That's great! How can I help you?", true));
                history.add(new Message("I'm interested in the Senior Marketing role.", false));
                break;

            case "u002": // Jane Smith
                history.add(new Message("Can we reschedule the meeting?", false));
                history.add(new Message("Sure, how about Friday?", true));
                history.add(new Message("Sounds good to me.", false));
                history.add(new Message("Thanks.", false));
                break;

            case "u003": // Charlie Watson
                history.add(new Message("Hi, is there any update on my application for the Data Analyst role?", false));
                history.add(new Message("Hi! We’re still reviewing applications.", true));
                history.add(new Message("Should have an update in 2–3 days.", true));
                history.add(new Message("Alright, thank you for the update.", false));
                history.add(new Message("No problem!", true));
                history.add(new Message("I’ll reach out once we have a decision.", true));
                break;

            default:
                history.add(new Message("This is a new conversation.", false));
                break;
        }
        return history;
    }
}