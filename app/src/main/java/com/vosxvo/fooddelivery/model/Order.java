package com.vosxvo.fooddelivery.model;

import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("id")
    private int id;
    @SerializedName("canceled")
    private boolean status;
    @SerializedName("pending")
    private int pending;
    @SerializedName("total")
    private int total;
    @SerializedName("date")
    private String date;

    public Order(int id, boolean status, int pending, int total, String date) {
        this.id = id;
        this.status = status;
        this.pending = pending;
        this.total = total;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
