package com.vosxvo.fooddelivery.module;

import com.vosxvo.fooddelivery.api.API;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(ActivityComponent.class)
public class RetrofitModule {

    @Provides
    public API provideAPI(Retrofit retrofit) {
        return retrofit.create(API.class);
    }

    @Provides
    public Retrofit provideRetrofit(GsonConverterFactory factory, String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(factory)
                .build();
    }

    @Provides
    public GsonConverterFactory provideConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    public String provideBaseUrl() {
        return API.MAIN_API_BASE_URL;
    }
}
