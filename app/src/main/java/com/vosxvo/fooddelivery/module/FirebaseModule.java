package com.vosxvo.fooddelivery.module;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vosxvo.fooddelivery.callback.SimpleCallback;
import com.vosxvo.fooddelivery.chat.Conversation;
import com.vosxvo.fooddelivery.chat.Message;
import com.vosxvo.fooddelivery.chat.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;

@Module
@InstallIn(ActivityComponent.class)
public class FirebaseModule {

    private List<User> userList;

    public void getUserList() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    userList.add(ds.getValue(User.class));
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

    public void chat(String message, String email) {
    }

    public void chatUid(String message, String uid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();    // Get current user uid
        String conversationKey = reference.child("conversations").push().getKey();  // Create new node in conversations and get this key
        long current = System.currentTimeMillis()/1000; // Get current time

        User.Conversation conversation = new User.Conversation(conversationKey, message, current, null);
        reference.child("users").child(myUid).child("conversations").child(uid);
    }

    @Provides
    public static FirebaseModule getInstance() {
        return FirebaseHelper.INSTANCE;
    }

    private static class FirebaseHelper {
        private static final FirebaseModule INSTANCE = new FirebaseModule();
    }
}
