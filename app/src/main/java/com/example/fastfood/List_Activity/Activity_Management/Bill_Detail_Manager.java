package com.example.fastfood.List_Activity.Activity_Management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fastfood.Adapter.Bill_Detail_Adapter;
import com.example.fastfood.Model.Bill_Detail;
import com.example.fastfood.R;
import com.example.fastfood.databinding.ActivityBillDetailManagerBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Bill_Detail_Manager extends AppCompatActivity {
    private ActivityBillDetailManagerBinding binding;
    Bill_Detail_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail_manager);
        binding = ActivityBillDetailManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String billId = getIntent().getStringExtra("billId");

        binding.rcvBillDetail.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Bill_Detail> options =
                new FirebaseRecyclerOptions.Builder<Bill_Detail>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("Bill_Detail")
                                .orderByChild("bill_Id")
                                .equalTo(billId), Bill_Detail.class)
                        .build();

        adapter = new Bill_Detail_Adapter(options,this);
        binding.rcvBillDetail.setAdapter(adapter);

        binding.btnBackBilldetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Bill_Detail_Manager.this, Bill_Manager.class));
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