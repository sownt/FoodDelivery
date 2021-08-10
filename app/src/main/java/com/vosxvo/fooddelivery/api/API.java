package com.vosxvo.fooddelivery.api;

import com.vosxvo.fooddelivery.model.ShopResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {
    String MAIN_API_BASE_URL = "https://flask-api-app-order.herokuapp.com/";

    @GET("shop/")
    Call<ShopResponse> getShopList();
}
