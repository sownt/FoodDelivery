package com.vosxvo.fooddelivery.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.api.API;
import com.vosxvo.fooddelivery.callback.SimpleCallback;
import com.vosxvo.fooddelivery.chat.Conversation;
import com.vosxvo.fooddelivery.chat.Message;
import com.vosxvo.fooddelivery.chat.User;
import com.vosxvo.fooddelivery.module.FirebaseModule;

import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignUpFragment extends Fragment {
    private TextInputLayout nickName;
    private TextInputLayout email;
    private TextInputLayout password;

    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nickName = view.findViewById(R.id.name_signUp_field);
        email = view.findViewById(R.id.email_signUp_field);
        password = view.findViewById(R.id.password_signUp_field);
        Button signUp = view.findViewById(R.id.sign_up_button);

        firebaseAuth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(v -> signUpWithEmail(
                email.getEditText().getText().toString(),
                password.getEditText().getText().toString()
        ));
    }

    private void updateNickName(String name) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) return;
        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        user.updateProfile(changeRequest)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // TODO : update name successful
                        switchActivity();
                    } else {
                        // TODO : failde
                    }
                });
    }

    private void signUpWithEmail(String email, String password) {
        Log.e("Food Delivery", "start create");
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        updateNickName(
                                nickName.getEditText().getText().toString()
                        );
                    } else {
                        Log.e("Food Delivery", "sign up failed");
                    }
                });
    }

    private void switchActivity() {
        putInfoToFirebase();
        ((SimpleCallback) requireActivity()).callback(null);
    }

    private void putInfoToFirebase() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        User user1 = new User(user.getDisplayName(), user.getEmail(), false);
        reference.child("users").child(user.getUid())
                .setValue(user1);

        String conversationKey = reference.child("conversations").push().getKey();
        long currentTime = System.currentTimeMillis()/1000;
        String WELCOME_MESSAGE = "Welcome to Food Delivery!";

        User.Conversation c1 = new User.Conversation(conversationKey, WELCOME_MESSAGE, currentTime, "Food Delivery");
        Conversation c2 = new Conversation(WELCOME_MESSAGE, currentTime);
        Message message = new Message(WELCOME_MESSAGE, API.ADMIN_UID, currentTime);

        reference.child("conversations").child(conversationKey)
                .setValue(c2);
        reference.child("conversations").child(conversationKey).child("messages").push()
                .setValue(message);
        reference.child("users").child(user.getUid()).child("conversations").child(API.ADMIN_UID)
                .setValue(c1);
        reference.child("users").child(API.ADMIN_UID).child("conversations").child(user.getUid())
                .setValue(c1);
    }
}
