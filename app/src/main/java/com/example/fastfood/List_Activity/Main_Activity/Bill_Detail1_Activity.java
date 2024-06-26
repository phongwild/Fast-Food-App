package com.example.fastfood.List_Activity.Main_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.fastfood.Adapter.Bill_Detail_Adapter;
import com.example.fastfood.Model.Bill_Detail;
import com.example.fastfood.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Bill_Detail1_Activity extends AppCompatActivity {
    Bill_Detail_Adapter adapter;
    ImageView btnback;
    RecyclerView rcvbillDetail;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail1);
        btnback = findViewById(R.id.btn_back_billdetail);
        rcvbillDetail = findViewById(R.id.rcvBillDetail);

        String billId = getIntent().getStringExtra("billId");

        rcvbillDetail.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Bill_Detail> options =
                new FirebaseRecyclerOptions.Builder<Bill_Detail>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("Bill_Detail")
                                .orderByChild("bill_Id")
                                .equalTo(billId), Bill_Detail.class)
                        .build();

        adapter = new Bill_Detail_Adapter(options,this);
        rcvbillDetail.setAdapter(adapter);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Bill_Detail1_Activity.this, Payment_Confirmation_Activity.class));
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