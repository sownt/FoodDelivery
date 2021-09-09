package com.vosxvo.fooddelivery.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.adapter.FoodAdapter;
import com.vosxvo.fooddelivery.api.API;
import com.vosxvo.fooddelivery.model.Food;
import com.vosxvo.fooddelivery.model.Shop;
import com.vosxvo.fooddelivery.module.DataModule;
import com.vosxvo.fooddelivery.util.CartHelper;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class FoodActivity extends AppCompatActivity {
    private ImageView foodImg;
    private TextView foodName;
    private TextView foodPrice;
    private Button cart;
    private ImageView shopImg;
    private TextView shopName;
    private TextView shopAddress;
    private ImageButton call;
    private RecyclerView inShop;

    private String callAction;

    @Inject
    public DataModule dataModule;
    @Inject
    public API api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        foodImg = findViewById(R.id.food_activity_img);
        foodName = findViewById(R.id.food_activity_name);
        foodPrice = findViewById(R.id.food_activity_price);
        cart = findViewById(R.id.food_activity_add);
        shopImg = findViewById(R.id.food_activity_shop_img);
        shopName = findViewById(R.id.food_activity_shop);
        shopAddress = findViewById(R.id.food_activity_address);
        call = findViewById(R.id.food_activity_call);
        inShop = findViewById(R.id.food_activity_recommend);

        inShop.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Toolbar toolbar = findViewById(R.id.food_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Detail");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle data = getIntent().getExtras();
        Food food = Food.fromBundle(data);

        getShopInfo(food);

        Glide.with(this)
                .load(API.MAIN_API_BASE_URL + food.getImage())
                .centerCrop()
                .into(foodImg);
        foodName.setText(food.getName());
        foodPrice.setText(food.getPrice("VND"));

        cart.setOnClickListener(v -> addToCart(data));
        call.setOnClickListener(v -> {
            if (callAction.isEmpty()) return;
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(callAction));
            startActivity(intent);
        });
    }

    private void getShopInfo(Food food) {
        if (food == null) return;
        Call<Shop> task = api.getShop(food.getShop());
        task.enqueue(new Callback<Shop>() {
            @Override
            public void onResponse(Call<Shop> call, Response<Shop> response) {
                Shop shop = response.body();
                if (shop != null) {
                    Glide.with(FoodActivity.this)
                            .load(API.MAIN_API_BASE_URL + shop.getImage())
                            .centerCrop()
                            .into(shopImg);
                    shopName.setText(shop.getName());
                    shopAddress.setText(shop.getAddress());
                    callAction = "tel:0" + shop.getPhone();
                    inShop.setAdapter(new FoodAdapter(shop.getFoods(), food1 -> {
                        Intent intent = new Intent(FoodActivity.this, FoodActivity.class);
                        intent.putExtras(food1.toBundle());
                        startActivity(intent);
                    }, FoodAdapter.PORTRAIL_VIEW_TYPE));
                }
            }

            @Override
            public void onFailure(Call<Shop> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void addToCart(Bundle bundle) {
        Food food = Food.fromBundle(bundle);
        View view = getLayoutInflater().inflate(R.layout.add_to_cart_bottom_sheet, null);
        ImageView foodImg = view.findViewById(R.id.cbs_img);
        TextView foodName = view.findViewById(R.id.cbs_food_name);
        TextView foodPrice = view.findViewById(R.id.cbs_food_price);
        MaterialButton cart = view.findViewById(R.id.cbs_cart);

        new CartHelper().addFood(food);

        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.CustomBottomSheetDialog);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();

        Glide.with(view).load(API.MAIN_API_BASE_URL + food.getImage())
                .centerCrop().into(foodImg);
        foodName.setText(food.getName());
        foodPrice.setText(food.getPrice("VND"));
        cart.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });
    }
}
