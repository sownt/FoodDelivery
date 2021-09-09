package com.vosxvo.fooddelivery.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.adapter.FoodMinimalAdapter;
import com.vosxvo.fooddelivery.model.Food;
import com.vosxvo.fooddelivery.model.Order;
import com.vosxvo.fooddelivery.module.DataModule;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OrderDetailActivity extends AppCompatActivity {
    private TextView orderId;
    private TextView orderDate;
    private TextView orderStatus;
    private TextView orderSubtotal;
    private TextView orderDiscount;
    private TextView orderShipping;
    private TextView orderTotal;
    private RecyclerView orderList;
    private Button func;

    @Inject
    public DataModule dataModule;

    public static final String ORDER_STATUS[] = {
            "Pending",
            "Completed",
            "Cancelled"
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderId = findViewById(R.id.order_detail_id);
        orderDate = findViewById(R.id.order_detail_date);
        orderStatus = findViewById(R.id.order_detail_status);
        orderSubtotal = findViewById(R.id.order_detail_subtotal);
        orderTotal = findViewById(R.id.order_detail_total);
        orderList = findViewById(R.id.order_detail_list);
        func = findViewById(R.id.order_detail_button);

        Toolbar toolbar = findViewById(R.id.order_detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Order Detail");
        }

        int id = getIntent().getIntExtra("id", 1);
        Order item = getOrderItem(id);
        orderId.setText("Order ID: " + item.getId());
        orderDate.setText("Order date: " + item.getDate());
        orderStatus.setText(ORDER_STATUS[item.getType()]);
        orderSubtotal.setText(item.getTotal("VND"));
        orderTotal.setText(item.getTotal("VND"));
        orderList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        orderList.setAdapter(new FoodMinimalAdapter(buildOrders(item)));
    }

    private HashMap<Food, Integer> buildOrders(Order item) {
        Food[] foods = dataModule.getFoods();
        Order.Bill[] bills = item.getBills();
        HashMap<Food, Integer> temp = new HashMap<>();
        for (Order.Bill bill : bills) {
            temp.put(foods[bill.getId()], bill.getQuantity());
        }
        return temp;
    }

    @NonNull
    private Order getOrderItem(int id) {
        Order[] orders = dataModule.getOrders();
        int n = orders.length;
        for (int i = 0; i < n; ++i) {
            if (orders[i].getId() == id) {
                return orders[i];
            }
        }
        return new Order();
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
}
