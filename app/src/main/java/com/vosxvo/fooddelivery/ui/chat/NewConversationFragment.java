package com.vosxvo.fooddelivery.ui.chat;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.callback.SimpleCallback;
import com.vosxvo.fooddelivery.chat.Conversation;
import com.vosxvo.fooddelivery.chat.User;
import com.vosxvo.fooddelivery.ui.ChatActivity;

public class NewConversationFragment extends Fragment {
    private EditText address;
    private ImageButton done;

    public NewConversationFragment() {
        super(R.layout.fragment_new_conversation);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        address = view.findViewById(R.id.to_address);
        done = view.findViewById(R.id.to_done);

        address.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        View blankView = view.findViewById(R.id.new_conversation_blank);

        Toolbar toolbar = view.findViewById(R.id.new_conversation_toolbar);
        toolbar.setTitle("New message");
//        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        done.setOnClickListener(v -> {
            if (address.getText().toString().equals(currentUser.getEmail())) {
                Snackbar.make(blankView, "It's your email!", Snackbar.LENGTH_SHORT).show();
            } else {
                getUid(
                        address.getText().toString(),
                        bundle -> {
                            if (bundle == null) {
                                Snackbar.make(blankView, "User not found!", Snackbar.LENGTH_SHORT).show();
                            } else {
                                String uid = bundle.getString("key");
                                String name = bundle.getString("name");
                                getConversation(uid, result -> {
                                    if (result == null) {
                                        createConversation(uid, name);
                                    } else {
                                        Bundle extras = new Bundle();
                                        extras.putString("id", result.getString("id"));
                                        extras.putString("name", name);
                                        ((ChatActivity) getActivity()).switchToChat(extras);
                                    }
                                });
                            }
                        }
                );
            }
        });
    }

    private void createConversation(String uid, String name) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String key = reference.child("conversations").push().getKey();
        long current = System.currentTimeMillis()/1000;
        Conversation conversation = new Conversation("", current);
        User.Conversation conversation1 = new User.Conversation(key, "", current, currentUser.getDisplayName());
        User.Conversation conversation2 = new User.Conversation(key, "", current, name);
        reference.child("conversations").child(key).setValue(conversation);
        reference.child("users").child(uid).child("conversations").child(currentUser.getUid())
                .setValue(conversation1);
        reference.child("users").child(currentUser.getUid()).child("conversations").child(uid)
                .setValue(conversation2);

        Bundle bundle = new Bundle();
        bundle.putString("id", key);
        bundle.putString("name", name);
        ((ChatActivity) getActivity()).switchToChat(bundle);
    }

    public void getConversation(String uid, SimpleCallback callback) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("users").child(currentUser.getUid()).child("conversations")
                .orderByKey().equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User.Conversation conversation = dataSnapshot.getValue(User.Conversation.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id", conversation.getID());
                        callback.callback(bundle);
                    }
                } else {
                    callback.callback(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getUid(String email, SimpleCallback callback) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("users").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Bundle bundle = new Bundle();
                        bundle.putString("key", dataSnapshot.getKey());
                        User user = dataSnapshot.getValue(User.class);
                        bundle.putString("name", user.getName());
                        callback.callback(bundle);
                    }
                } else {
                    callback.callback(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
