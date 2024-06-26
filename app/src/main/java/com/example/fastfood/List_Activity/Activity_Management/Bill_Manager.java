package com.example.fastfood.List_Activity.Activity_Management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fastfood.Adapter.Bill_Adapter;
import com.example.fastfood.List_Activity.Main_Activity.MainActivity_Admin;
import com.example.fastfood.Model.Bill;
import com.example.fastfood.R;
import com.example.fastfood.databinding.ActivityBillManagerBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Bill_Manager extends AppCompatActivity {
    private ActivityBillManagerBinding binding;
    Bill_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_manager);
        binding = ActivityBillManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.rcvBill.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Bill> options =
                new FirebaseRecyclerOptions.Builder<Bill>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Bill")
                                .orderByChild("purchase_Date"), Bill.class)
                        .build();

        adapter = new Bill_Adapter(options);
        binding.rcvBill.setAdapter(adapter);

        binding.btnBackBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Bill_Manager.this, MainActivity_Admin.class));
            }
        });
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