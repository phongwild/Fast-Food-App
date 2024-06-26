package com.example.fastfood.Fragment.Fragment_User;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fastfood.Adapter.Notification_Adapter;
import com.example.fastfood.Model.Notification;
import com.example.fastfood.R;
import com.example.fastfood.databinding.FragmentNotificationBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Fragment_User_Notification extends Fragment {
    public Fragment_User_Notification() {

    }
    Notification_Adapter adapter;
    String userId;
    private FragmentNotificationBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        userId = getUserIdFromSharedPreferences();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.rcvNotificationUser.setLayoutManager(layoutManager);
        FirebaseRecyclerOptions<Notification> options =
                new FirebaseRecyclerOptions.Builder<Notification>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("Notification")
                                .orderByChild("user_Id")
                                .equalTo(userId), Notification.class)
                        .build();


        adapter = new Notification_Adapter(options);
        binding.rcvNotificationUser.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }

    private String getUserIdFromSharedPreferences(){
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("userId","");
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