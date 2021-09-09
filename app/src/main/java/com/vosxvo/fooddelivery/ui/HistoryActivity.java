package com.vosxvo.fooddelivery.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.ui.history.HistoryAllFragment;
import com.vosxvo.fooddelivery.ui.history.HistoryCancelledFragment;
import com.vosxvo.fooddelivery.ui.history.HistoryCompletedFragment;
import com.vosxvo.fooddelivery.ui.history.HistoryPendingFragment;

public class HistoryActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 pager2;
    private FragmentStateAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        tabLayout = findViewById(R.id.history_tabs);
        pager2 = findViewById(R.id.history_pager2);

        Toolbar toolbar = findViewById(R.id.history_toolbar);
        toolbar.setTitle("My Orders");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new FragmentAdapter(getSupportFragmentManager(), this.getLifecycle());
        pager2.setAdapter(adapter);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("position")) {
            int pos = getIntent().getExtras().getInt("position", 0);
            if (pos >= 0 && pos < 4) pager2.setCurrentItem(pos);
        }

        new TabLayoutMediator(tabLayout, pager2, (tab, position) -> {
            if (position == 0) {
                tab.setText(R.string.all);
            } else if (position == 1) {
                tab.setText(R.string.pending);
            } else if (position == 2) {
                tab.setText(R.string.completed);
            } else if (position == 3) {
                tab.setText(R.string.cancelled);
            }
        }).attach();
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

    private static class FragmentAdapter extends FragmentStateAdapter {

        public FragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new HistoryAllFragment();
            } else if (position == 1) {
                return new HistoryPendingFragment();
            } else if (position == 2) {
                return new HistoryCompletedFragment();
            }
            return new HistoryCancelledFragment();
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}
