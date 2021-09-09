package com.vosxvo.fooddelivery.chat;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String name;
    private String email;
    private boolean online;

    public User() {
    }

    public User(String name, String email, boolean online) {
        this.name = name;
        this.email = email;
        this.online = online;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public static class Conversation {
        private String ID;
        private String lastMessage;
        private long lastTime;
        private String name;

        public Conversation() {
        }

        public Conversation(String ID, String lastMessage, long lastTime, String name) {
            this.ID = ID;
            this.lastMessage = lastMessage;
            this.lastTime = lastTime;
            this.name = name;
        }

        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("ID", ID);
            result.put("lastMessage", lastMessage);
            result.put("lastTime", lastTime);
            result.put("name", name);

            return result;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
