package com.example.fastfood.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fastfood.Model.Notification;
import com.example.fastfood.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class Notification_Adapter extends FirebaseRecyclerAdapter<Notification, Notification_Adapter.myViewHolder> {
    public Notification_Adapter(@NonNull FirebaseRecyclerOptions<Notification> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Notification model) {
        holder.text_notification.setText(model.getContent_Notification());
        holder.date_notification.setText(model.getSend_Date());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new myViewHolder(view);
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        TextView text_notification, date_notification;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            text_notification = itemView.findViewById(R.id.text_notification);
            date_notification = itemView.findViewById(R.id.date_notification);
        }
    }

}
