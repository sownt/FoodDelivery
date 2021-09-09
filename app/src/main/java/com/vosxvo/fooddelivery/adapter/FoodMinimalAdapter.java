package com.vosxvo.fooddelivery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.model.Food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FoodMinimalAdapter extends RecyclerView.Adapter<FoodMinimalAdapter.FoodMinimalViewHolder> {
    private HashMap<Food, Integer> orders;
    private List<Food> keySet;

    public FoodMinimalAdapter(HashMap<Food, Integer> orders) {
        this.orders = orders;
        keySet = new ArrayList<>(orders.keySet());
    }

    @NonNull
    @Override
    public FoodMinimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodMinimalViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_minimal, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull FoodMinimalViewHolder holder, int position) {
        holder.name.setText(keySet.get(position).getName());
        holder.note.setText(getNote(keySet.get(position)));
    }

    private String getNote(Food food) {
        return food.getPrice("VND") + "    x" + orders.get(food);
    }

    @Override
    public int getItemCount() {
        if (keySet != null) return keySet.size();
        return 0;
    }

    public static class FoodMinimalViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView note;

        public FoodMinimalViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_food_name);
            note = itemView.findViewById(R.id.item_food_note);
        }
    }
}
