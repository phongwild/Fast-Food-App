package com.example.fastfood.Fragment.Fragment_Admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fastfood.Adapter.Bill_Adapter;
import com.example.fastfood.Adapter.OrderConfirmation_Adapter;
import com.example.fastfood.Adapter.PaymentConfirmation_Adapter;
import com.example.fastfood.List_Activity.Main_Activity.Order_Confirmation_Activity;
import com.example.fastfood.List_Activity.Main_Activity.Payment_Confirmation_Activity;
import com.example.fastfood.Model.Bill;
import com.example.fastfood.R;
import com.example.fastfood.databinding.FragmentAdminBillBinding;
import com.example.fastfood.databinding.FragmentAdminChartBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Fragment_Admin_Home extends Fragment {
    public Fragment_Admin_Home() {

    }

    private FragmentAdminChartBinding binding;
    OrderConfirmation_Adapter orderConfirmationAdapter;
    PaymentConfirmation_Adapter paymentConfirmationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminChartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Tính doanh thu trong ngày
        updateTotalRevenueForCurrentDay();

        // rcv1
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.rcvOrderConfirmation.setLayoutManager(layoutManager);

        FirebaseRecyclerOptions<Bill> optionsForRcv1 =
                new FirebaseRecyclerOptions.Builder<Bill>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Bill")
                                .orderByChild("status").equalTo(0), Bill.class)
                        .build();

        orderConfirmationAdapter = new OrderConfirmation_Adapter(optionsForRcv1);
        binding.rcvOrderConfirmation.setAdapter(orderConfirmationAdapter);

        // rcv2
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManager1.setReverseLayout(true);
        layoutManager1.setStackFromEnd(true);
        binding.rcvPaymentConfirmation.setLayoutManager(layoutManager1);

        FirebaseRecyclerOptions<Bill> optionsForRcv2 =
                new FirebaseRecyclerOptions.Builder<Bill>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Bill")
                                .orderByChild("status").equalTo(1), Bill.class)
                        .build();

        paymentConfirmationAdapter = new PaymentConfirmation_Adapter(optionsForRcv2);
        binding.rcvPaymentConfirmation.setAdapter(paymentConfirmationAdapter);

        binding.SeeDetails1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Order_Confirmation_Activity.class));
            }
        });

        binding.SeeDetails2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Payment_Confirmation_Activity.class));
            }
        });

        return view;
    }

    // Hàm cập nhật doanh thu cho ngày hiện tại
    private void updateTotalRevenueForCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormat.format(calendar.getTime());

        FirebaseDatabase.getInstance().getReference().child("Bill")
                .orderByChild("purchase_Date").equalTo(currentDate)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        double totalRevenue = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Bill bill = dataSnapshot.getValue(Bill.class);
                            if (bill != null && bill.getStatus() == 2) {
                                totalRevenue += bill.getTotal_Amount();
                            }
                        }
                        // Hiển thị tổng doanh thu trên giao diện
                        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                        String formattedTotalRevenue = formatter.format(totalRevenue);
                        binding.total.setText(String.valueOf(formattedTotalRevenue));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý khi có lỗi xảy ra
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        orderConfirmationAdapter.startListening();
        paymentConfirmationAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        orderConfirmationAdapter.stopListening();
        paymentConfirmationAdapter.stopListening();
    }

}