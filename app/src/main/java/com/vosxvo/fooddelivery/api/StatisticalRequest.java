package com.vosxvo.fooddelivery.api;

import com.google.gson.annotations.SerializedName;

public class StatisticalRequest {
    private int completed;
    private int order_completed;

    public StatisticalRequest() {
    }

    public StatisticalRequest(int completed, int count) {
        this.completed = completed;
        this.order_completed = count;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getCount() {
        return order_completed;
    }

    public void setCount(int count) {
        this.order_completed = count;
    }

    public static class User {
        @SerializedName("user_email")
        private String user_email;

        public User() {
        }

        public User(String user_email) {
            this.user_email = user_email;
        }

        public String getUser_email() {
            return user_email;
        }

        public void setUser_email(String user_email) {
            this.user_email = user_email;
        }
    }
}
