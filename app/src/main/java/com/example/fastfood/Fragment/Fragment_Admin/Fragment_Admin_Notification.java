package com.example.fastfood.Fragment.Fragment_Admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fastfood.Adapter.Notification_Adapter;
import com.example.fastfood.Model.Notification;
import com.example.fastfood.R;
import com.example.fastfood.databinding.FragmentAdminNotificationBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Fragment_Admin_Notification extends Fragment {
    public Fragment_Admin_Notification() {

    }
    private FragmentAdminNotificationBinding binding;
    Notification_Adapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminNotificationBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.rcvNotification.setLayoutManager(layoutManager);
        FirebaseRecyclerOptions<Notification> options =
                new FirebaseRecyclerOptions.Builder<Notification>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Notification").orderByChild("send_Date"), Notification.class)
                        .build();

        adapter = new Notification_Adapter(options);
        binding.rcvNotification.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}