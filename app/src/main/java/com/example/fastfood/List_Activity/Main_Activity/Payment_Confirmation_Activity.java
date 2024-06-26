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
import com.example.fastfood.Model.Bill;
import com.example.fastfood.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Payment_Confirmation_Activity extends AppCompatActivity {
    PaymentConfirmation_Adapter paymentConfirmationAdapter;
    RecyclerView rcvPayment;
    ImageView btnback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);
        rcvPayment = findViewById(R.id.rcv_paymentconfirmation);
        btnback = findViewById(R.id.back_Payment_Confirmation);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setReverseLayout(true);
        layoutManager1.setStackFromEnd(true);
        rcvPayment.setLayoutManager(layoutManager1);

        FirebaseRecyclerOptions<Bill> optionsForRcv2 =
                new FirebaseRecyclerOptions.Builder<Bill>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Bill")
                                .orderByChild("status").equalTo(1), Bill.class)
                        .build();

        paymentConfirmationAdapter = new PaymentConfirmation_Adapter(optionsForRcv2);
        rcvPayment.setAdapter(paymentConfirmationAdapter);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Payment_Confirmation_Activity.this, MainActivity_Admin.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        paymentConfirmationAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        paymentConfirmationAdapter.stopListening();
    }
}