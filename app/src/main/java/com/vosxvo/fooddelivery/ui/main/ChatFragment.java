package com.vosxvo.fooddelivery.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.adapter.ChatAdapter;
import com.vosxvo.fooddelivery.callback.SimpleCallback;
import com.vosxvo.fooddelivery.ui.ChatActivity;

public class ChatFragment extends Fragment {
    private RecyclerView chatList;
    private ExtendedFloatingActionButton newMessage;
    private ChatAdapter adapter;
    private SimpleCallback itemClicked = bundle -> {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chatList = view.findViewById(R.id.chat_list);
        newMessage = view.findViewById(R.id.chat_new_message);
        adapter = new ChatAdapter(itemClicked);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        chatList.setLayoutManager(layoutManager);
        chatList.setAdapter(adapter);

        newMessage.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            startActivity(intent);
        });
    }
}
