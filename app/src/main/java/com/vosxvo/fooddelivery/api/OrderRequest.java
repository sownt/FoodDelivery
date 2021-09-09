package com.vosxvo.fooddelivery.api;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vosxvo.fooddelivery.model.Food;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderRequest {
    @Expose
    @SerializedName("user_email")
    private String email;
    @Expose
    @SerializedName("order")
    private Order[] order;
    private HashMap<Food, Integer> cart;
    private int totalPrice = 0;

    public void add(Food item) {
        if (cart.containsKey(item)) {
            up(item);
        } else {
            totalPrice += item.getPrice();
            cart.put(item, 1);
        }
    }

    public void remove(Food item) {
        if (!cart.containsKey(item)) return;
        int c = cart.get(item);
        totalPrice -= c * item.getPrice();
        cart.remove(item);
    }

    public void up(Food item) {
        if (!cart.containsKey(item)) return;
        totalPrice += item.getPrice();
        int c = cart.get(item);
        cart.put(item, c + 1);
    }

    public void down(Food item) {
        if (!cart.containsKey(item)) return;
        totalPrice -= item.getPrice();
        int c = cart.get(item);
        cart.put(item, c - 1);
    }

    public List<Food> getFoodList() {
        if (cart == null) return new ArrayList<>();
        return new ArrayList<>(cart.keySet());
    }

    public HashMap<Food, Integer> getCart() {
        return cart;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getPrice(String currency) {
        DecimalFormat decimalFormat = new DecimalFormat();
        switch (currency) {
            case "VND":
                decimalFormat.applyPattern("###,### đ");
                return decimalFormat.format(totalPrice);
            case "USD":
                decimalFormat.applyPattern("$0,000,000,000.00");
                return decimalFormat.format(totalPrice);
        }
        return String.valueOf(totalPrice);
    }

    public int getQuantity(Food item) {
        return cart.get(item);
    }

    public void build() {
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        order = new Order[cart.size()];
        int i = 0;
        for (Food item : cart.keySet()) {
            order[i++] = new Order(item.getId(), cart.get(item));
        }
    }

    public OrderRequest() {
        cart = new HashMap<>();
    }

    public static class Order {
        @SerializedName("food_id")
        private int foodId;
        @SerializedName("quantity")
        private int quantity;

        public Order(int foodId, int quantity) {
            this.foodId = foodId;
            this.quantity = quantity;
        }

        public int getFoodId() {
            return foodId;
        }

        public void setFoodId(int foodId) {
            this.foodId = foodId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
