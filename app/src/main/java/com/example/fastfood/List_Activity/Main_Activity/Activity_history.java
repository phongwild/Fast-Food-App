package com.example.fastfood.List_Activity.Main_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.fastfood.Adapter.History_adapter;
import com.example.fastfood.Model.Bill_Detail;
import com.example.fastfood.Model.ProductType;
import com.example.fastfood.R;
import com.example.fastfood.databinding.ActivityHistoryBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Activity_history extends AppCompatActivity {
    private ActivityHistoryBinding binding;
    History_adapter adapter;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userId = getUserIdFromSharedPreferences();
        binding.btnBackHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.rcvHistory.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Bill_Detail> options =
                new FirebaseRecyclerOptions.Builder<Bill_Detail>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Bill_Detail"),Bill_Detail.class)
                        .build();
        adapter = new History_adapter(options, this);
        adapter.setUserId(userId);
        binding.rcvHistory.setAdapter(adapter);
    }

    private String getUserIdFromSharedPreferences(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("UserData", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("userId","");
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}