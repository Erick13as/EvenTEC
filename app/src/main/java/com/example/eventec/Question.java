package com.example.eventec;

public class Question {
    private String id;
    private String userId;
    private String userName;
    private String text;

    public Question() {
        // Required empty constructor for Firebase
    }

    public Question(String id, String userId, String userName, String text) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }
}
