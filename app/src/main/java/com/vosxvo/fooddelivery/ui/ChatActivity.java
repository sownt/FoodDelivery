package com.vosxvo.fooddelivery.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.ui.chat.ConversationFragment;
import com.vosxvo.fooddelivery.ui.chat.NewConversationFragment;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_chat, NewConversationFragment.class, null)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_chat, ConversationFragment.class, bundle)
                    .commit();
        }
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

    public void switchToChat(Bundle bundle) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_chat, ConversationFragment.class, bundle)
                .commit();
    }

}
