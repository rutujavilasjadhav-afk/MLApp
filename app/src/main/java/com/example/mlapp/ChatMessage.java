package com.example.mlapp;

public class ChatMessage {

    public enum SenderType {
        USER,
        BOT,
        TYPING
    }

    public enum DepthLevel {
        QUICK,
        NORMAL,
        DEEP
    }

    private final String text;
    private final SenderType senderType;
    private final String timestamp;
    private final DepthLevel depthLevel;

    public ChatMessage(String text, SenderType senderType, String timestamp) {
        this(text, senderType, timestamp, DepthLevel.NORMAL);
    }

    public ChatMessage(String text, SenderType senderType, String timestamp, DepthLevel depthLevel) {
        this.text = text;
        this.senderType = senderType;
        this.timestamp = timestamp;
        this.depthLevel = depthLevel;
    }

    public String getText() {
        return text;
    }

    public SenderType getSenderType() {
        return senderType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public DepthLevel getDepthLevel() {
        return depthLevel;
    }
}