package com.vosxvo.fooddelivery.model;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;

public class Order {
    public static final int PENDING_STATUS = 0;
    public static final int COMPLETED_STATUS = 1;
    public static final int CANCELLED_STATUS = 2;

    @SerializedName("id")
    private int id;
    @SerializedName("date")
    private String date;
    @SerializedName("total")
    private int total;
    @SerializedName("canceled")
    private boolean cancelled = false;
    @SerializedName("status")
    private boolean confirm = false;
    @SerializedName("bill")
    private Bill[] bills;

    public int getType() {
        if (cancelled) return CANCELLED_STATUS;
        else if (confirm) return COMPLETED_STATUS;
        return PENDING_STATUS;
    }

    public Order() {
    }

    public Order(int id, String date, int total, boolean cancelled, boolean confirm) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.cancelled = cancelled;
        this.confirm = confirm;
    }

    public Order(int id, String date, int total, boolean cancelled, boolean confirm, Bill[] bills) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.cancelled = cancelled;
        this.confirm = confirm;
        this.bills = bills;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotal() {
        return total;
    }

    public String getTotal(String currency) {
        DecimalFormat decimalFormat = new DecimalFormat();
        switch (currency) {
            case "VND":
                decimalFormat.applyPattern("###,### đ");
                return decimalFormat.format(total);
            case "USD":
                decimalFormat.applyPattern("$0,000,000,000.00");
                return decimalFormat.format(total);
        }
        return String.valueOf(total);
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public Bill[] getBills() {
        return bills;
    }

    public void setBills(Bill[] bills) {
        this.bills = bills;
    }

    public static class Bill {
        @SerializedName("food_id")
        private int id;
        @SerializedName("food_name")
        private String name;
        @SerializedName("quantity")
        private int quantity;

        public Bill() {
        }

        public Bill(int id, String name, int quantity) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
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
