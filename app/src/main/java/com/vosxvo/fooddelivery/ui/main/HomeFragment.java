package com.vosxvo.fooddelivery.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.adapter.FoodAdapter;
import com.vosxvo.fooddelivery.adapter.TopBannerAdapter;
import com.vosxvo.fooddelivery.api.API;
import com.vosxvo.fooddelivery.callback.FoodItemClickedCallback;
import com.vosxvo.fooddelivery.model.ChatItem;
import com.vosxvo.fooddelivery.model.Food;
import com.vosxvo.fooddelivery.module.DataModule;
import com.vosxvo.fooddelivery.ui.ChatActivity;
import com.vosxvo.fooddelivery.ui.FoodActivity;
import com.vosxvo.fooddelivery.ui.HistoryActivity;
import com.vosxvo.fooddelivery.ui.MainActivity;
import com.vosxvo.fooddelivery.util.CartHelper;
import com.vosxvo.fooddelivery.util.FoodHelper;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment implements FoodItemClickedCallback {
    @Inject
    public API api;
    @Inject
    public DataModule dataModule;

    private LinearLayout mainLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView forYouList = view.findViewById(R.id.home_for_you);
        RecyclerView foodList = view.findViewById(R.id.home_food);
        ViewPager2 topBanner = view.findViewById(R.id.top_banner_pager);
        TabLayout bannerIndicator = view.findViewById(R.id.top_banner_indicator);
        mainLayout = view.findViewById(R.id.home_main_layout);

        TopBannerAdapter bannerAdapter = new TopBannerAdapter();
        topBanner.setAdapter(bannerAdapter);
        new TabLayoutMediator(bannerIndicator, topBanner, (tab, position) -> {}).attach();

        foodList.setAdapter(new FoodAdapter(new FoodHelper().getData(), this, FoodAdapter.NORMAL_VIEW_TYPE));
        foodList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        forYouList.setAdapter(new FoodAdapter(new FoodHelper().getData(), this, FoodAdapter.HORIZONTAL_VIEW_TYPE));
        forYouList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    int currentItem = topBanner.getCurrentItem();
                    currentItem = (currentItem + 1) % 8;
                    topBanner.setCurrentItem(currentItem);
                });
            }
        }, 500, 5000);

        ImageButton homeGenerate = view.findViewById(R.id.home_generate);
        homeGenerate.setOnClickListener(v -> generateMenu());

        ImageButton myOrders = view.findViewById(R.id.home_my_orders);
        myOrders.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HistoryActivity.class);
            getActivity().startActivity(intent);
        });

        ImageButton homeChat = view.findViewById(R.id.home_chat);
        homeChat.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            startActivity(intent);
        });

        ImageButton homeSupport = view.findViewById(R.id.home_support);
        homeSupport.setOnClickListener(v -> getSupport());
    }

    private void generateMenu() {
        Food[] foods = dataModule.getFoods();
        if (foods == null || foods.length == 0) {
            Snackbar.make(mainLayout, "Nothing available!", Snackbar.LENGTH_SHORT).show();
        }
        int length = foods.length;

        for (int i = 0; i < 3; ++i) {
            int randomInt = ThreadLocalRandom.current().nextInt(0, length);
            new CartHelper().addFood(foods[randomInt]);
        }

        Snackbar.make(mainLayout, "Generate successfully!", Snackbar.LENGTH_LONG)
                .setAction("Go To Cart", v -> {
                    ((MainActivity) requireActivity()).switchPager(1);
                }).show();
    }

    private void getSupport() {
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("conversations")
                .child(API.ADMIN_UID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ChatItem chatItem = snapshot.getValue(ChatItem.class);
                        chatItem.setUid(snapshot.getKey());

                        Bundle bundle = new Bundle();
                        bundle.putString("id", chatItem.getId());
                        bundle.putString("name", chatItem.getName());
                        bundle.putString("uid", chatItem.getUid());

                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void callback(@NonNull Food food) {
        Intent intent = new Intent(requireActivity(), FoodActivity.class);
        intent.putExtras(food.toBundle());
        startActivity(intent);
    }
}
