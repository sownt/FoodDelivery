package com.vosxvo.fooddelivery.api;

import com.vosxvo.fooddelivery.BuildConfig;
import com.vosxvo.fooddelivery.model.Food;
import com.vosxvo.fooddelivery.model.Order;
import com.vosxvo.fooddelivery.model.Shop;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface API {
    String MAIN_API_BASE_URL = BuildConfig.MAIN_API_BASE_URL;
    String ADMIN_UID = BuildConfig.ADMIN_UID;

    @GET("/shop/")
    Call<Shop[]> getAllShop();

    @GET("/shop/{shop_id}")
    Call<Shop> getShop(@Path("shop_id") int id);

    @GET("/food/")
    Call<Food[]> getAllFood();

    @GET("/shop/{shop_id}")
    Call<Food[]> getFoodList(@Path("shop_id") int id);

    @POST("/history/")
    Call<Order[]> getOrderHistory(@Body Order.User user);

    @POST("/history/{order_id}")
    Call<Order> getHistory(@Path("order_id") int id, @Body Order.User user);

    @POST("/order/")
    Call<Object> order(@Body OrderRequest request);

    @POST("/statistical/")
    Call<StatisticalRequest> getStatistical(@Body StatisticalRequest.User user);
}
