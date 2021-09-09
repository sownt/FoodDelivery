package com.vosxvo.fooddelivery.ui.chat;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.adapter.MessageAdapter;
import com.vosxvo.fooddelivery.chat.Conversation;
import com.vosxvo.fooddelivery.chat.Message;
import com.vosxvo.fooddelivery.chat.User;

public class ConversationFragment extends Fragment {
    private RecyclerView chatList;
    private EditText message;
    private ImageButton send;
    private MessageAdapter adapter;

    public ConversationFragment() {
        super(R.layout.fragment_conversation);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chatList = view.findViewById(R.id.chat_rcv);
        message = view.findViewById(R.id.chat_message);
        send = view.findViewById(R.id.chat_send);

        Bundle bundle = requireArguments();
        String id = bundle.getString("id");
        String name = bundle.getString("name");
        String uid = bundle.getString("uid");

        Toolbar toolbar = view.findViewById(R.id.conversation_toolbar);
        toolbar.setTitle(name);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new MessageAdapter(id);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);

        chatList.setLayoutManager(layoutManager);
        chatList.setAdapter(adapter);

        send.setOnClickListener(v -> {
            String content = message.getText().toString();
            if (content.isEmpty()) return;
            sendMessage(id, content, name, uid);
            message.setText("");
        });
    }

    private void sendMessage(String id, String content, String name, String uid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        long current = System.currentTimeMillis()/1000;
        String mKey = reference.child("conversations").child(id).child("messages").push().getKey();

        Message message = new Message(content, user.getUid(), current);
        Conversation conversation1 = new Conversation(content, current);
        User.Conversation conversation2 = new User.Conversation(id, content, current, name);

        reference.child("conversations")
                .child(id)
                .child("messages")
                .child(mKey)
                .updateChildren(message.toMap());

        reference.child("conversations")
                .child(id)
                .updateChildren(conversation1.toMap());

        reference.child("users")
                .child(user.getUid())
                .child("conversations")
                .child(uid)
                .updateChildren(conversation2.toMap());
    }
}
