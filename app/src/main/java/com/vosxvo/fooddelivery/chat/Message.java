package com.vosxvo.fooddelivery.chat;

import java.util.HashMap;
import java.util.Map;

public class Message {
    private String content;
    private String sender;
    private long time;

    public Message() {
    }

    public Message(String content, String sender, long time) {
        this.content = content;
        this.sender = sender;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("sender", sender);
        result.put("time", time);

        return result;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
