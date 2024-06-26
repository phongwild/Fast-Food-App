package com.example.fastfood.List_Activity.Main_Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.fastfood.Adapter.OrderConfirmation_Adapter;
import com.example.fastfood.Adapter.PaymentConfirmation_Adapter;
import com.example.fastfood.Fragment.Fragment_Admin.Fragment_Admin_Home;
import com.example.fastfood.Model.Bill;
import com.example.fastfood.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Order_Confirmation_Activity extends AppCompatActivity {
    OrderConfirmation_Adapter orderConfirmationAdapter;
    RecyclerView rcvOderConfirm;
    ImageView btnback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        rcvOderConfirm = findViewById(R.id.rcv_orderconfirmation);
        btnback = findViewById(R.id.back_Order_Confirmation);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rcvOderConfirm.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Bill> optionsForRcv1 =
                new FirebaseRecyclerOptions.Builder<Bill>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Bill")
                                .orderByChild("status").equalTo(0), Bill.class)
                        .build();

        orderConfirmationAdapter = new OrderConfirmation_Adapter(optionsForRcv1);
        rcvOderConfirm.setAdapter(orderConfirmationAdapter);
        orderConfirmationAdapter.notifyDataSetChanged();

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Order_Confirmation_Activity.this, MainActivity_Admin.class));
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        orderConfirmationAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        orderConfirmationAdapter.stopListening();
    }
}