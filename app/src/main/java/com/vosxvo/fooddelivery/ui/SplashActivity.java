package com.vosxvo.fooddelivery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.api.API;
import com.vosxvo.fooddelivery.model.Food;
import com.vosxvo.fooddelivery.model.Order;
import com.vosxvo.fooddelivery.module.DataModule;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class SplashActivity extends AppCompatActivity {
    @Inject
    public DataModule dataModule;
    @Inject
    public API api;

    private ConstraintLayout mainLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mainLayout = findViewById(R.id.activity_splash);

        new Handler().postDelayed(this::checkSignedIn, 1500);
    }

    private void checkSignedIn() {
        if (isSignedIn()) { // if signed in, get data and switch to MainActivity
            getFoodList();
        } else {    // if not, switch to log in / sign up
            switchActivity(new Intent(this, StarterActivity.class));
        }
    }

    private boolean isSignedIn() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) return true;
        return false;
    }

    private void switchActivity(@NonNull Intent intent) {
        startActivity(intent);
        finishAffinity();   // also close starter activity
    }

    private void getFoodList() {
        Call<Food[]> task = api.getAllFood();
        task.enqueue(new Callback<Food[]>() {
            @Override
            public void onResponse(Call<Food[]> call, Response<Food[]> response) {
                if (dataModule != null) dataModule.setFoods(response.body());
                getHistoryOrder();
            }

            @Override
            public void onFailure(Call<Food[]> call, Throwable t) {
                Log.e("Get Food list : ", t.getMessage());
                Snackbar.make(mainLayout, "GET DATA FAILED!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("TRY AGAIN", v -> getFoodList())
                        .setActionTextColor(getResources().getColor(R.color.app_primary))
                        .show();
            }
        });
    }

    private void getHistoryOrder() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;
        Order.User user = new Order.User(currentUser.getEmail());
        Call<Order[]> task = api.getOrderHistory(user);

        task.enqueue(new Callback<Order[]>() {
            @Override
            public void onResponse(Call<Order[]> call, Response<Order[]> response) {
                Order[] result = response.body();
                if (result == null) {   // No order had made, then switch to MainActivity
                    switchActivity();
                    return;
                }
                dataModule.setOrders(result);

                for (int i = 0; i < result.length; ++i) {   // Get specify order data
                    int id = result[i].getId();
                    getHistory(id, i, user);
                }
                switchActivity();
            }

            @Override
            public void onFailure(Call<Order[]> call, Throwable t) {
                Log.e("Get History Order : ", t.getMessage());
                Snackbar.make(mainLayout, "GET DATA FAILED!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("TRY AGAIN", v -> getHistoryOrder())
                        .setActionTextColor(getResources().getColor(R.color.app_primary))
                        .show();
            }

            private void switchActivity() {
                SplashActivity.this.switchActivity(
                        new Intent(SplashActivity.this, MainActivity.class)
                );
            }
        });
    }

    private void getHistory(int id, int index, Order.User user) {
        Call<Order> task = api.getHistory(id, user);
        task.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                Order result = response.body();
                if (result != null) dataModule.getOrders()[index] = result;
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.e("Get History : ", t.getMessage());
                Snackbar.make(mainLayout, "GET DATA FAILED!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("TRY AGAIN", v -> getHistory(id, index, user))
                        .setActionTextColor(getResources().getColor(R.color.app_primary))
                        .show();
            }
        });
    }
}
