package com.example.chatbot;


public class Message {


    private String message;
    private boolean isSelf;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSelf() {
        return isSelf;
    }
    public void setSelf(boolean self) {
        isSelf = self;
    }
}

