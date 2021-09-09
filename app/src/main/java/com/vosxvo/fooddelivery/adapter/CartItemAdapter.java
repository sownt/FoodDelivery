package com.vosxvo.fooddelivery.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.api.API;
import com.vosxvo.fooddelivery.api.OrderRequest;
import com.vosxvo.fooddelivery.callback.SimpleCallback;
import com.vosxvo.fooddelivery.model.Food;
import com.vosxvo.fooddelivery.module.DataModule;

import java.text.DecimalFormat;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    private List<Food> foods;
    private SimpleCallback callback;

    public CartItemAdapter(List<Food> foods) {
        this.foods = foods;
    }

    public void setCallback(SimpleCallback callback) {
        this.callback = callback;
    }

    private void callback(int price) {
        if (callback == null) return;
        Bundle bundle = new Bundle();
        bundle.putInt("price", price);
        callback.callback(bundle);
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartItemViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        Food food = foods.get(position);
        OrderRequest orderRequest = DataModule.getInstance().getOrderRequest();

        Glide.with(holder.itemView)
                .load(API.MAIN_API_BASE_URL + food.getImage())
                .centerCrop()
                .into(holder.cartImg);
        holder.foodName.setText(food.getName());
        holder.foodPrice.setText(food.getPrice("VND"));
        holder.cartQa.setText(String.valueOf(orderRequest.getQuantity(food)));

        holder.cartIns.setOnClickListener(v -> {
            Food selected = foods.get(holder.getAdapterPosition());
            orderRequest.up(selected);
            holder.cartQa.setText(String.valueOf(orderRequest.getQuantity(selected)));
            callback(orderRequest.getTotalPrice());
        });

        holder.cartDec.setOnClickListener(v -> {
            Food selected = foods.get(holder.getAdapterPosition());
            int quantity = orderRequest.getQuantity(selected);
            if (quantity > 1) {
                orderRequest.down(selected);
                holder.cartQa.setText(String.valueOf(orderRequest.getQuantity(selected)));
            } else {
                orderRequest.remove(selected);
                foods.remove(selected);
                notifyItemRemoved(holder.getAdapterPosition());
            }
            callback(orderRequest.getTotalPrice());
        });

        holder.cartRemove.setOnClickListener(v -> {
            Food selected = foods.get(holder.getAdapterPosition());
            orderRequest.remove(selected);
            foods.remove(selected);
            notifyItemRemoved(holder.getAdapterPosition());
            callback(orderRequest.getTotalPrice());
        });
    }

    @Override
    public int getItemCount() {
        if (foods != null) return foods.size();
        return 0;
    }

    public static final class CartItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView cartImg;
        public TextView foodName;
        public TextView foodPrice;
        public ImageButton cartDec;
        public TextView cartQa;
        public ImageButton cartIns;
        public Button cartRemove;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cartImg = itemView.findViewById(R.id.cart_img);
            foodName = itemView.findViewById(R.id.cart_food_name);
            foodPrice = itemView.findViewById(R.id.cart_food_price);
            cartDec = itemView.findViewById(R.id.cart_dec);
            cartQa = itemView.findViewById(R.id.cart_qa);
            cartIns = itemView.findViewById(R.id.cart_ins);
            cartRemove = itemView.findViewById(R.id.cart_remove);
        }
    }
}
