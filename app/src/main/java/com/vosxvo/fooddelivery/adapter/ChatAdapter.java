package com.vosxvo.fooddelivery.adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.callback.SimpleCallback;
import com.vosxvo.fooddelivery.chat.Conversation;
import com.vosxvo.fooddelivery.chat.User;
import com.vosxvo.fooddelivery.model.ChatItem;
import com.vosxvo.fooddelivery.module.FirebaseModule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatItemViewHolder> {
    private List<ChatItem> chatItemList;
    private SimpleCallback callback;

    private FirebaseUser user;
    private DatabaseReference reference;

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (chatItemList != null) chatItemList.clear();
            else chatItemList = new ArrayList<>();
            for (DataSnapshot item : snapshot.getChildren()) {
                ChatItem chatItem = item.getValue(ChatItem.class);
                chatItem.setUid(item.getKey());
                chatItemList.add(chatItem);
            }
            notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.w("loadChats:onCancelled", error.getDetails());
        }
    };

    public ChatAdapter(SimpleCallback callback) {
        this.callback = callback;
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("users")
                .child(user.getUid())
                .child("conversations")
                .addValueEventListener(listener);
    }

    @NonNull
    @Override
    public ChatItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatItemViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ChatItemViewHolder holder, int position) {
        holder.chatName.setText(chatItemList.get(position).getName());
        holder.lastTime.setText(getTimeAgo(chatItemList.get(position).getLastTime()));
        holder.preview.setText(chatItemList.get(position).getLastMessage());

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", chatItemList.get(position).getId());
            bundle.putString("name", chatItemList.get(position).getName());
            bundle.putString("uid", chatItemList.get(position).getUid());

            callback.callback(bundle);
        });
    }

    @Override
    public int getItemCount() {
        if (chatItemList != null) return chatItemList.size();
        return 0;
    }

    public static String getTimeAgo(long time) {

        final DateFormat sdfTime = new SimpleDateFormat("h:mm aa", Locale.ENGLISH);
        final DateFormat sdf = new SimpleDateFormat("d MMM 'at' h:mm aa", Locale.ENGLISH);
        final DateFormat sdfY = new SimpleDateFormat("d MMM yyyy 'at' h:mm aa", Locale.ENGLISH);

        final int SECOND_MILLIS = 1000;
        final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        final int DAY_MILLIS = 24 * HOUR_MILLIS;

        if (time < 1000000000000L) {
            time *= 1000;
        }

        Calendar today = Calendar.getInstance();

        Calendar timeCal = Calendar.getInstance();
        timeCal.setTimeInMillis(time);

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "Just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "1 min";
        } else if (diff < 59 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " mins";
        } else if (diff < 2 * HOUR_MILLIS) {
            return "1 hr";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hrs";
        } else if (diff < 48 * HOUR_MILLIS) {
            return ("Yesterday at " + capsAMtoSmall(sdfTime.format(timeCal.getTime())));
        } else if (today.get(Calendar.YEAR) == timeCal.get(Calendar.YEAR)) {
            return capsAMtoSmall(sdf.format(timeCal.getTime()));
        } else {
            return capsAMtoSmall(sdfY.format(timeCal.getTime()));
        }
    }

    private static String capsAMtoSmall(String time) {
        return time.replace("AM", "am").replace("PM","pm");
    }

    protected class ChatItemViewHolder extends RecyclerView.ViewHolder {
        public TextView chatName;
        public TextView lastTime;
        public TextView preview;

        public ChatItemViewHolder(@NonNull View itemView) {
            super(itemView);
            chatName = itemView.findViewById(R.id.chat_name);
            lastTime = itemView.findViewById(R.id.chat_lastTime);
            preview = itemView.findViewById(R.id.chat_preview);
        }
    }
}
