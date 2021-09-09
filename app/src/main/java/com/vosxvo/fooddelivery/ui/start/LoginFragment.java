package com.vosxvo.fooddelivery.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.callback.SimpleCallback;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {
    private TextInputLayout email;
    private TextInputLayout password;
    private Button logIn;

    private FirebaseAuth firebaseAuth;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email = view.findViewById(R.id.email_signIn_field);
        password = view.findViewById(R.id.password_signIn_field);
        logIn = view.findViewById(R.id.sign_in_button);

        firebaseAuth = FirebaseAuth.getInstance();

        logIn.setOnClickListener(v -> {
            signIn(
                    email.getEditText().getText().toString(),
                    password.getEditText().getText().toString()
            );
        });
    }

    private void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ((SimpleCallback) requireActivity()).callback(null);
                    } else {
                    }
                });

    }
}