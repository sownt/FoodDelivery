package com.vosxvo.fooddelivery.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.adapter.FoodAdapter;
import com.vosxvo.fooddelivery.api.API;
import com.vosxvo.fooddelivery.api.StatisticalRequest;
import com.vosxvo.fooddelivery.module.DataModule;
import com.vosxvo.fooddelivery.ui.FoodActivity;
import com.vosxvo.fooddelivery.ui.HistoryActivity;
import com.vosxvo.fooddelivery.ui.StarterActivity;

import java.text.DecimalFormat;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout toolbarLayout;
    private TextView profileName;
    private TextView profileEmail;
    private LinearLayout logout;
    private TextView ordersNum;
    private TextView ordersPrice;
    private LinearLayout orderPending;
    private LinearLayout orderCompleted;
    private LinearLayout orderCancelled;
    private LinearLayout orderAll;
    private RecyclerView recently;

    private FirebaseAuth firebaseAuth;

    @Inject
    public API api;
    @Inject
    public DataModule dataModule;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appBarLayout = view.findViewById(R.id.profile_appbar);
        toolbarLayout = view.findViewById(R.id.profile_collapsing_toolbar);
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        logout = view.findViewById(R.id.profile_logout);
        ordersNum = view.findViewById(R.id.profile_order_num);
        ordersPrice = view.findViewById(R.id.profile_order_price);
        orderPending = view.findViewById(R.id.profile_order_pending);
        orderCompleted = view.findViewById(R.id.profile_order_completed);
        orderCancelled = view.findViewById(R.id.profile_order_cancelled);
        orderAll = view.findViewById(R.id.profile_order_all);
        recently = view.findViewById(R.id.profile_recently);

        recently.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recently.setAdapter(new FoodAdapter(dataModule.getFoods(), food -> {
            Intent intent = new Intent(requireActivity(), FoodActivity.class);
            intent.putExtras(food.toBundle());
            startActivity(intent);
        }, FoodAdapter.PORTRAIL_VIEW_TYPE));

        firebaseAuth = FirebaseAuth.getInstance();
        setUser();
        setCollapsingToolbar();
        setOnClick();
    }

    private void setOnClick() {
        logout.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), StarterActivity.class);
            startActivity(intent);
            firebaseAuth.signOut();
            requireActivity().finishAffinity();
        });

        orderPending.setOnClickListener(v -> myOrders(1));
        orderCompleted.setOnClickListener(v -> myOrders(2));
        orderCancelled.setOnClickListener(v -> myOrders(3));
        orderAll.setOnClickListener(v -> myOrders(0));
    }

    private void myOrders(int position) {
        Intent intent = new Intent(getActivity(), HistoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    private void setCollapsingToolbar() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            private boolean isExpanded = true;
            private int scrollRage = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (scrollRage == -1) {
                    scrollRage = appBarLayout.getTotalScrollRange();
                }

                if (scrollRage + verticalOffset == 0) {
                    toolbarLayout.setTitle("Profile");
                    isExpanded = true;
                } else {
                    toolbarLayout.setTitle(" ");
                    isExpanded = false;
                }

            }
        });
    }

    private void setUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            profileName.setText(user.getDisplayName());
            profileEmail.setText(user.getEmail());
        }

        StatisticalRequest.User u = new StatisticalRequest.User(user.getEmail());
        Call<StatisticalRequest> task = api.getStatistical(u);
        task.enqueue(new Callback<StatisticalRequest>() {
            @Override
            public void onResponse(Call<StatisticalRequest> call, Response<StatisticalRequest> response) {
                StatisticalRequest result = response.body();
                if (result != null) {
                    ordersNum.setText(getStringOrderNum(result.getCount()));
                    ordersPrice.setText(getStringOrderPrice(result.getCompleted()));
                }
            }

            @Override
            public void onFailure(Call<StatisticalRequest> call, Throwable t) {

            }
        });
    }

    private String getStringOrderPrice(int completed) {
        return getPrice("VND", completed);
    }

    private String getStringOrderNum(int count) {
        if (count == 0) {
            return "No order";
        } else if (count == 1) {
            return "1 order";
        }
        return count + " orders";
    }

    public String getPrice(String currency, int price) {
        DecimalFormat decimalFormat = new DecimalFormat();
        switch (currency) {
            case "VND":
                decimalFormat.applyPattern("###,### đ");
                return decimalFormat.format(price);
            case "USD":
                decimalFormat.applyPattern("$0,000,000,000.00");
                return decimalFormat.format(price);
        }
        return String.valueOf(price);
    }
}
