package com.vosxvo.fooddelivery.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.ui.main.CartFragment;
import com.vosxvo.fooddelivery.ui.main.ChatFragment;
import com.vosxvo.fooddelivery.ui.main.HomeFragment;
import com.vosxvo.fooddelivery.ui.main.ProfileFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private static final int NUM_PAGES = 4;
    private ViewPager2 pager;
    private TabLayout tabLayout;
    private FragmentStateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = findViewById(R.id.main_pager);
        tabLayout = findViewById(R.id.main_tab);
        adapter = new FragmentAdapter(getSupportFragmentManager(), this.getLifecycle());

        pager.setAdapter(adapter);
        pager.setUserInputEnabled(false);

        new TabLayoutMediator(tabLayout, pager, (tab, position) -> {
            if (position == 0) {
                tab.setText(R.string.home);
                tab.setIcon(R.drawable.ic_round_home_24);
            } else if (position == 1) {
                tab.setText(R.string.cart);
                tab.setIcon(R.drawable.ic_round_shopping_cart_24);
            } else if (position == 2) {
                tab.setText(R.string.chat);
                tab.setIcon(R.drawable.ic_round_chat_bubble_outline_24);
            } else if (position == 3) {
                tab.setText(R.string.profile);
                tab.setIcon(R.drawable.ic_round_perm_identity_24);
            }
        }).attach();
    }

    public void switchPager(int position) {
        pager.setCurrentItem(position);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private static class FragmentAdapter extends FragmentStateAdapter {

        public FragmentAdapter(@NonNull
                               @org.jetbrains.annotations.NotNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @org.jetbrains.annotations.NotNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new HomeFragment();
            } else if (position == 1) {
                return new CartFragment();
            } else if (position == 2) {
                return new ChatFragment();
            }
            return new ProfileFragment();
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}