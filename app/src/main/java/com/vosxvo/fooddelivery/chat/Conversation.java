package com.vosxvo.fooddelivery.chat;

import java.util.HashMap;
import java.util.Map;

public class Conversation {
    private String lastMessage;
    private long lastTime;

    public Conversation() {
    }

    public Conversation(String lastMessage, long lastTime) {
        this.lastMessage = lastMessage;
        this.lastTime = lastTime;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("lastMessage", lastMessage);
        result.put("lastTime", lastTime);

        return result;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
}
