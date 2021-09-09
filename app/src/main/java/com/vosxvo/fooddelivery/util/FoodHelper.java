package com.vosxvo.fooddelivery.util;

import android.util.Log;

import androidx.annotation.NonNull;

import com.vosxvo.fooddelivery.api.API;
import com.vosxvo.fooddelivery.model.Food;
import com.vosxvo.fooddelivery.module.DataModule;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodHelper {

    public FoodHelper() {}

    /**
     * Get food data from server and put it in {@link DataModule}
     * @param api retrofit api
     */
    public void refresh(API api) {
        Call<Food[]> task = api.getAllFood();
        task.enqueue(new Callback<Food[]>() {
            @Override
            public void onResponse(Call<Food[]> call, Response<Food[]> response) {
                Food[] result = response.body();
                if (result != null && result.length != 0) {
                    DataModule.getInstance().setFoods(result);
                } else {
                    Log.e("Data - Food", "null");
                }
            }

            @Override
            public void onFailure(Call<Food[]> call, Throwable t) {
                Log.e("Data - Food", t.getMessage());
            }
        });
    }

    /**
     * Get food data from DataModule Singleton
     * @return food data
     */
    @NonNull
    public Food[] getData() {
        Food[] foods = DataModule.getInstance().getFoods();
        if (foods == null || foods.length == 0) {
            return new Food[0];
        }
        return foods;
    }
}
