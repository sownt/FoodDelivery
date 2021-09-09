package com.vosxvo.fooddelivery.module;

import com.vosxvo.fooddelivery.api.OrderRequest;
import com.vosxvo.fooddelivery.model.Food;
import com.vosxvo.fooddelivery.model.Order;
import com.vosxvo.fooddelivery.model.Shop;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@Module
@InstallIn(ActivityComponent.class)
public class DataModule {
    private Food[] foods;
    private Shop[] shops;
    private Order[] orders;
    private OrderRequest orderRequest;

    public Food[] getFoods() {
        if (foods == null) foods = new Food[0];
        return foods;
    }

    public void setFoods(Food[] foods) {
        this.foods = foods;
    }

    public Shop[] getShops() {
        if (shops == null) shops = new Shop[0];
        return shops;
    }

    public void setShops(Shop[] shops) {
        this.shops = shops;
    }

    public Order[] getOrders() {
        if (orders == null) orders = new Order[0];
        return orders;
    }

    public void setOrders(Order[] orders) {
        this.orders = orders;
    }

    public OrderRequest getOrderRequest() {
        if (orderRequest == null) orderRequest = new OrderRequest();
        return orderRequest;
    }

    public void resetOrderRequest() {
        orderRequest = new OrderRequest();
    }

    public void setOrderRequest(OrderRequest orderRequest) {
        this.orderRequest = orderRequest;
    }

    @Provides
    public static DataModule getInstance() {
        return DataSingleton.INSTANCE;
    }

    private static class DataSingleton {
        private static final DataModule INSTANCE = new DataModule();
    }
}