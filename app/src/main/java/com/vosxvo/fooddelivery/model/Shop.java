package com.vosxvo.fooddelivery.model;

import com.google.gson.annotations.SerializedName;

public class Shop {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("phone")
    private int phone;
    @SerializedName("address")
    private String address;
    @SerializedName("food")
    private Food[] foods;
    @SerializedName("shop_pictures")
    private String image;

    public Shop(int id, String name, int phone, String address, Food[] foods) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.foods = foods;
    }

    public Shop(int id, String name, int phone, String address, Food[] foods, String image) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.foods = foods;
        this.image = image;
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

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Food[] getFoods() {
        return foods;
    }

    public void setFoods(Food[] foods) {
        this.foods = foods;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}