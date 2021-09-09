package com.vosxvo.fooddelivery.model;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.util.Objects;

public class Food implements BundleModel {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PRICE = "price";
    public static final String FOOD_PICTURE = "image";
    public static final String SHOP_ID = "shop";

    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private int price;
    @SerializedName("food_pictures")
    private String image;
    @SerializedName("shop_id")
    private int shop;

    public Food() {
    }

    public Food(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Food(int id, String name, int price, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public Food(int id, String name, int price, String image, int shop) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.shop = shop;
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

    public int getPrice() {
        return price;
    }

    public String getPrice(String currency) {
        DecimalFormat decimalFormat = new DecimalFormat();
        switch (currency) {
            case "VND":
                decimalFormat.applyPattern("###,### đ");
                return decimalFormat.format(price);
            case "USD":
                decimalFormat.applyPattern("$0,000,000,000.00");
                return decimalFormat.format(price);
        }
        return String.valueOf(price);
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getShop() {
        return shop;
    }

    public void setShop(int shop) {
        this.shop = shop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return id == food.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt(ID, getId());
        bundle.putString(NAME, getName());
        bundle.putInt(PRICE, getPrice());
        bundle.putString(FOOD_PICTURE, getImage());
        bundle.putInt(SHOP_ID, getShop());

        return bundle;
    }

    @Override
    public void setData(@NonNull Bundle data) {
        setId(data.getInt(ID));
        setName(data.getString(NAME));
        setPrice(data.getInt(PRICE));
        setImage(data.getString(FOOD_PICTURE));
        setShop(data.getInt(SHOP_ID));
    }

    public static Bundle toBundle(@NonNull Food food) {
        return food.toBundle();
    }

    public static Food fromBundle(@NonNull Bundle data) {
        Food food = new Food();
        food.setData(data);
        return food;
    }
}

