package com.vosxvo.fooddelivery.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vosxvo.fooddelivery.R;
import com.vosxvo.fooddelivery.chat.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private static final int LEFT_MESSAGE_TYPE = 0;
    private static final int RIGHT_MESSAGE_TYPE = 1;

    private String userUid;
    private String id;
    private List<Message> messageList;

    private ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (messageList != null) {
                messageList.clear();
            } else {
                messageList = new ArrayList<>();
            }
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                messageList.add(dataSnapshot.getValue(Message.class));
            }
            notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e("Firebase", error.getMessage());
        }
    };

    public MessageAdapter(String id) {
        this.id = id;
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("conversations").child(id).child("messages").addValueEventListener(listener);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == LEFT_MESSAGE_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.timestamp.setText(messageList.get(position).getTime() + "");
        holder.message.setText(messageList.get(position).getContent());

        holder.itemView.setOnClickListener(v -> {
            if (holder.timestamp.getVisibility() == View.VISIBLE) {
                holder.timestamp.setVisibility(View.GONE);
            } else {
                holder.timestamp.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (messageList != null) return messageList.size();
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (userUid.equals(messageList.get(position).getSender()))
            return RIGHT_MESSAGE_TYPE;
        return LEFT_MESSAGE_TYPE;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView timestamp;
        public TextView message;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            timestamp = itemView.findViewById(R.id.message_timestamp);
            message = itemView.findViewById(R.id.message_content);
        }
    }
}
