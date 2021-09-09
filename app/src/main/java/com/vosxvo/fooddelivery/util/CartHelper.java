package com.vosxvo.fooddelivery.util;

import com.vosxvo.fooddelivery.model.Food;
import com.vosxvo.fooddelivery.module.DataModule;

public class CartHelper {

    public void addFood(Food food) {
        DataModule.getInstance().getOrderRequest().add(food);
    }

    public void remove(Food food) {
        DataModule.getInstance().getOrderRequest().remove(food);
    }

    public void increase(Food food) {
        DataModule.getInstance().getOrderRequest().up(food);
    }

    public void decrease(Food food) {
        DataModule.getInstance().getOrderRequest().down(food);
    }

    public void build() {
        DataModule.getInstance().getOrderRequest().build();
    }
}
