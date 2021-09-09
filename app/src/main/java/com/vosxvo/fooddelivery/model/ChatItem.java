package com.vosxvo.fooddelivery.model;

public class ChatItem {
    private String uid;
    private String id;
    private String name;
    private String lastMessage;
    private long lastTime;

    public ChatItem() {
    }

    public ChatItem(String id, String name, String lastMessage, long lastTime) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.lastTime = lastTime;
    }

    public ChatItem(String uid, String id, String name, String lastMessage, long lastTime) {
        this.uid = uid;
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.lastTime = lastTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
