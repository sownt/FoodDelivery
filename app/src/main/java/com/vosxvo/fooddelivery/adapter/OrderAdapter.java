package com.vosxvo.fooddelivery.adapter;

import android.os.Bundle;
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
import com.vosxvo.fooddelivery.callback.SimpleCallback;
import com.vosxvo.fooddelivery.model.Food;
import com.vosxvo.fooddelivery.model.Order;
import com.vosxvo.fooddelivery.module.DataModule;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderItemViewHolder> {
    public static final String ORDER_STATUS[] = {
            "Pending",
            "Completed",
            "Cancelled"
    };

    private Order[] orders;
    private SimpleCallback call;

    public OrderAdapter(SimpleCallback callback) {
        this.orders = DataModule.getInstance().getOrders();
        this.call = callback;
    }

    public OrderAdapter(int itemType, SimpleCallback callback) {
        if (itemType == Order.PENDING_STATUS) {
            buildPendingList();
        } else if (itemType == Order.COMPLETED_STATUS) {
            buildCompletedList();
        } else if (itemType == Order.CANCELLED_STATUS) {
            buildCancelledList();
        }
        this.call = callback;
    }

    private void buildCancelledList() {
        Order[] all = DataModule.getInstance().getOrders();
        List<Order> need = new ArrayList<>();
        for (int i = 0; i < all.length; ++i) {
            if (all[i].getType() == Order.CANCELLED_STATUS) {
                need.add(all[i]);
            }
        }
        orders = new Order[need.size()];
        need.toArray(orders);
    }

    private void buildCompletedList() {
        Order[] all = DataModule.getInstance().getOrders();
        List<Order> need = new ArrayList<>();
        for (int i = 0; i < all.length; ++i) {
            if (all[i].getType() == Order.COMPLETED_STATUS) {
                need.add(all[i]);
            }
        }
        orders = new Order[need.size()];
        need.toArray(orders);
    }

    private void buildPendingList() {
        Order[] all = DataModule.getInstance().getOrders();
        List<Order> need = new ArrayList<>();
        for (int i = 0; i < all.length; ++i) {
            if (all[i].getType() == Order.PENDING_STATUS) {
                need.add(all[i]);
            }
        }
        orders = new Order[need.size()];
        need.toArray(orders);
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderItemViewHolder(
                LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        holder.status.setText(ORDER_STATUS[orders[position].getType()]);
        holder.title.setText(getTitle(orders[position]));
        holder.quantity.setText(getQuantity(orders[position]));
        holder.price.setText(orders[position].getTotal("VND"));
        Glide.with(holder.itemView)
                .load(getFoodImg(orders[position].getBills()[0].getId()))
                .centerCrop()
                .into(holder.img);

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id", orders[holder.getAdapterPosition()].getId());
            call.callback(bundle);
        });
    }

    @NonNull
    private String getTitle(Order item) {
        if (item == null) return "";

        StringBuilder builder = new StringBuilder();
        builder.append(item.getBills()[0].getName());
        int n = item.getBills().length;

        if (n == 1) {
            return builder.toString();
        }

        builder.append(" and ");
        builder.append(n - 1);
        builder.append(" others.");
        return builder.toString();
    }

    private String getQuantity(Order order) {
        if (order == null) return "";
        int quantity = order.getBills().length;
        if (quantity == 1) return quantity + " item";
        return quantity + " items";
    }

    private String getFoodImg(int id) {
        Food[] foods = DataModule.getInstance().getFoods();
        if (id == 0) return "";
        return API.MAIN_API_BASE_URL + foods[id - 1].getImage();
    }

    @Override
    public int getItemCount() {
        if (orders != null) return orders.length;
        return 0;
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        public TextView status;
        public ImageView img;
        public TextView title;
        public TextView quantity;
        public TextView price;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.item_order_status);
            img = itemView.findViewById(R.id.item_order_img);
            title = itemView.findViewById(R.id.item_order_title);
            quantity = itemView.findViewById(R.id.item_order_quantity);
            price = itemView.findViewById(R.id.item_order_price);
        }
    }
}
