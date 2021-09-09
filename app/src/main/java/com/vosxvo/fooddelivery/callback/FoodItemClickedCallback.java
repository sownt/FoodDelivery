package com.vosxvo.fooddelivery.callback;

import androidx.annotation.NonNull;

import com.vosxvo.fooddelivery.model.Food;

public interface FoodItemClickedCallback {
    void callback(@NonNull Food food);
}
