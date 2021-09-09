package com.vosxvo.fooddelivery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.api.API;
import com.vosxvo.fooddelivery.callback.FoodItemClickedCallback;
import com.vosxvo.fooddelivery.model.Food;

import java.text.DecimalFormat;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodItemViewHolder> {
    public static final int NORMAL_VIEW_TYPE = 0;
    public static final int HORIZONTAL_VIEW_TYPE = 1;
    public static final int PORTRAIL_VIEW_TYPE = 2;

    private Food[] foods;
    private FoodItemClickedCallback itemClicked;
    private int viewType = NORMAL_VIEW_TYPE;

    public FoodAdapter(Food[] foods, FoodItemClickedCallback itemClicked) {
        this.foods = foods;
        this.itemClicked = itemClicked;
    }

    public FoodAdapter(Food[] foods, FoodItemClickedCallback itemClicked, int viewType) {
        this.foods = foods;
        this.itemClicked = itemClicked;
        this.viewType = viewType;
    }

    @NonNull
    @Override
    public FoodAdapter.FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == NORMAL_VIEW_TYPE) {
            return new FoodItemViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_food_large, parent, false)
            );
        } else if (viewType == HORIZONTAL_VIEW_TYPE) {
            return new FoodItemViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_food_small, parent, false)
            );
        }
        return new FoodItemViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_food_portrail, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemViewHolder holder, int position) {
        Food food = foods[position];

        Glide.with(holder.itemView)
                .load(API.MAIN_API_BASE_URL + food.getImage())
                .centerCrop()
                .placeholder(R.drawable.no_image)
                .into(holder.foodImg);
        holder.foodName.setText(food.getName());
        holder.foodPrice.setText(food.getPrice("VND"));

        View.OnClickListener clickListener = v -> itemClicked.callback(food);
        holder.itemView.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        if (foods != null) return foods.length;
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return this.viewType;
    }

    public static final class FoodItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView foodImg;
        public TextView foodName;
        public TextView foodPrice;

        public FoodItemViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImg = itemView.findViewById(R.id.item_food_img);
            foodName = itemView.findViewById(R.id.item_food_name);
            foodPrice = itemView.findViewById(R.id.item_food_price);
        }
    }
}