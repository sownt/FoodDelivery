package com.vosxvo.fooddelivery.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.adapter.CartItemAdapter;
import com.vosxvo.fooddelivery.adapter.FoodMinimalAdapter;
import com.vosxvo.fooddelivery.api.API;
import com.vosxvo.fooddelivery.api.OrderRequest;
import com.vosxvo.fooddelivery.model.Order;
import com.vosxvo.fooddelivery.module.DataModule;
import com.vosxvo.fooddelivery.util.OrderHelper;

import java.text.DecimalFormat;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class CartFragment extends Fragment {
    private RecyclerView cartList;
    private TextView totalPrice;
    private Button buy;
    private CartItemAdapter adapter;
    private LinearLayout cartNothing;

    @Inject
    public DataModule dataModule;
    @Inject
    public API api;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cartList = view.findViewById(R.id.cart_list);
        totalPrice = view.findViewById(R.id.cart_total);
        buy = view.findViewById(R.id.cart_buy);
        cartNothing = view.findViewById(R.id.cart_nothing);

        DecimalFormat decimalFormat = new DecimalFormat("###,### đ");
        totalPrice.setText(decimalFormat.format(dataModule.getOrderRequest().getTotalPrice()));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        cartList.setLayoutManager(layoutManager);

        setCart();

        buy.setOnClickListener(v -> placeOrder());
    }

    @Override
    public void onResume() {
        super.onResume();
        setCart();
        DecimalFormat decimalFormat = new DecimalFormat("###,### đ");
        totalPrice.setText(decimalFormat.format(dataModule.getOrderRequest().getTotalPrice()));
        adapter = new CartItemAdapter(dataModule.getOrderRequest().getFoodList());
        cartList.setAdapter(adapter);
        adapter.setCallback(bundle -> {
            if (bundle != null && bundle.containsKey("price")) {
                totalPrice.setText(decimalFormat.format(bundle.getInt("price")));
                setCart();
            }
        });
    }

    private void setCart() {
        if (dataModule.getOrderRequest().getFoodList().size() != 0) {
            if (cartNothing.getVisibility() == View.VISIBLE) cartNothing.setVisibility(View.GONE);
        } else {
            if (cartNothing.getVisibility() == View.GONE) cartNothing.setVisibility(View.VISIBLE);
        }
    }

    private void placeOrder() {
        OrderRequest request = dataModule.getOrderRequest();
        request.build();

        View view = getLayoutInflater().inflate(R.layout.order_bottom_sheet, null);
        RecyclerView orderList = view.findViewById(R.id.order_bs_list);
        TextView totalPrice = view.findViewById(R.id.order_bs_price);
        Button confirm = view.findViewById(R.id.order_bs_confirm);

        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(view);
        dialog.show();

        orderList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        orderList.setAdapter(new FoodMinimalAdapter(request.getCart()));
        totalPrice.setText(request.getPrice("VND"));

        confirm.setOnClickListener(v -> {
            dialog.dismiss();
            Call<Object> task = api.order(request);
            task.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    notifyOrderSuccessfully();
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });
        });
    }

    private void notifyOrderSuccessfully() {
        View view = getLayoutInflater().inflate(R.layout.order_bottom_sheet_successful, null);
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(view);
        dialog.show();
        new Handler().postDelayed(dialog::dismiss, 3000);

        new OrderHelper().refresh(
                api,
                new Order.User(FirebaseAuth.getInstance().getCurrentUser().getEmail())
        );
        dataModule.resetOrderRequest();

        setCart();
    }
}
