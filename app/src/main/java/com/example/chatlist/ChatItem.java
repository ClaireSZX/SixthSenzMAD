package com.example.chatlist;
public class ChatItem {
    private String userId;
    private String userName;
    private String lastMessage;
    private String timestamp;

    public ChatItem(String userId, String userName, String lastMessage, String timestamp) {
        this.userId = userId;
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getLastMessage() { return lastMessage; }
    public String getTimestamp() { return timestamp; }
}