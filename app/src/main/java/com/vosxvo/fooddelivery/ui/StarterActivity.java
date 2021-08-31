package com.vosxvo.fooddelivery.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.callback.SimpleCallback;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StarterActivity extends FragmentActivity implements SimpleCallback {
    private static final int NUM_PAGES = 2;
    private ViewPager2 pager;
    private TabLayout tabLayout;
    private FragmentStateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);

        pager = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.starter_tab);

        adapter = new FragmentAdapter(this);
        pager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, pager, (tab, position) -> {
            if (position == 0) tab.setText("Sign In");
            else tab.setText("Sign Up");
        }).attach();
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    @Override
    public void callback(@Nullable Bundle bundle) {
        startActivity(new Intent(this, SplashActivity.class));
        finishAffinity();
    }

    private static class FragmentAdapter extends FragmentStateAdapter {
        public FragmentAdapter(@NonNull
                               @org.jetbrains.annotations.NotNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @org.jetbrains.annotations.NotNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0)
                return new LoginFragment();
            return new SignUpFragment();
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}
