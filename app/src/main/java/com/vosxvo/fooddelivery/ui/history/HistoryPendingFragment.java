package com.vosxvo.fooddelivery.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.adapter.OrderAdapter;
import com.vosxvo.fooddelivery.model.Order;
import com.vosxvo.fooddelivery.ui.OrderDetailActivity;

public class HistoryPendingFragment extends Fragment {
    private RecyclerView list;
    private FrameLayout nothing;

    public HistoryPendingFragment() {
        super(R.layout.fragment_history_pending);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = view.findViewById(R.id.order_pending);
        nothing = view.findViewById(R.id.order_pending_nothing);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        list.setLayoutManager(layoutManager);

        OrderAdapter adapter = new OrderAdapter(Order.PENDING_STATUS, bundle -> {
            Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        if (adapter.getItemCount() == 0) {
            nothing.setVisibility(View.VISIBLE);
        } else {
            nothing.setVisibility(View.GONE);
        }
        list.setAdapter(adapter);
    }
}
