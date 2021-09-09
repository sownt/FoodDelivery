package com.vosxvo.fooddelivery.util;

import android.util.Log;

import com.vosxvo.fooddelivery.api.API;
import com.vosxvo.fooddelivery.model.Order;
import com.vosxvo.fooddelivery.module.DataModule;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHelper {
    public OrderHelper() {}

    public void refresh(API api, Order.User user) {
        Call<Order[]> task = api.getOrderHistory(user);
        task.enqueue(new Callback<Order[]>() {
            @Override
            public void onResponse(Call<Order[]> call, Response<Order[]> response) {
                Order[] orders = response.body();
                if (orders != null && orders.length != 0) {
                    DataModule.getInstance().setOrders(orders);
                } else {
                    Log.e("Data - Order", "null");
                }

                for (int i = 0; i < orders.length; ++i) {
                    int id = orders[i].getId();
                    getOrder(api, user, id, i);
                }
            }

            @Override
            public void onFailure(Call<Order[]> call, Throwable t) {
                Log.e("Data - Order", t.getMessage());
            }
        });
    }

    public void getOrder(API api, Order.User user, int id, int i) {
        Call<Order> task = api.getHistory(id, user);
        task.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                Order result = response.body();
                if (result != null) {
                    DataModule.getInstance().getOrders()[i] = result;
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {

            }
        });
    }

    public Order[] getData() {
        Order[] orders = DataModule.getInstance().getOrders();
        if (orders == null || orders.length == 0) {
            return null;
        }
        return orders;
    }
}
