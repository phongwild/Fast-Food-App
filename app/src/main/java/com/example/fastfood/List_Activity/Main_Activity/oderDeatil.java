package com.example.fastfood.List_Activity.Main_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.fastfood.Adapter.oderDeltai_Adapter;
import com.example.fastfood.Fragment.Fragment_User.Fragment_User_Cart;
import com.example.fastfood.Model.Bill;
import com.example.fastfood.Model.Bill_Detail;
import com.example.fastfood.Model.Cart;
import com.example.fastfood.R;
import com.example.fastfood.databinding.ActivityOderDeatilBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class oderDeatil extends AppCompatActivity implements oderDeltai_Adapter.OnDataClickListener {
    private SharedPreferences shareRef;
    private String userId,fullName,address,bill_Id;
    private int phoneNumber;
    private ActivityOderDeatilBinding binding;
    private List<Bill_Detail> bill_detailList = new ArrayList<>();
    oderDeltai_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder_deatil);
        binding = ActivityOderDeatilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backOrderDeltail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(oderDeatil.this, MainActivity_User.class));
            }
        });
        shareRef = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        userId = shareRef.getString("userId","");
        phoneNumber = shareRef.getInt("phoneNumber",0);
        fullName = shareRef.getString("fullName","");
        address = shareRef.getString("address","");

        binding.rcvOrder.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("Cart")
                                .orderByChild("user_Id")
                                .equalTo(userId), Cart.class)
                        .build();
        adapter = new oderDeltai_Adapter(options);
        binding.rcvOrder.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnDataClickListener(this);

        binding.FullNameOderDeatail.setText(fullName);
        binding.PhoneNumberOderDeatail.setText("0" + String.valueOf(phoneNumber));
        binding.AddressOderDeatail.setText(address);

        binding.Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddDataToBill_Detail();
                AddDataToBill();
            }
        });

    }

    @Override
    public void onDataClicked(String productId, int total, int price, int quantity, int totalCost) {
        binding.TotailOderDeatail.setText(String.valueOf(totalCost));

        Bill_Detail billDetail = new Bill_Detail();
        billDetail.setInvoice_detail_id(UUID.randomUUID().toString());
        billDetail.setUser_Id(userId);
        billDetail.setId(productId);
        billDetail.setCart_quantity(quantity);
        billDetail.setPrice(price);
        billDetail.setTotal_details(total);
        billDetail.setBill_Id(generateBillId());
        bill_detailList.add(billDetail);
    }

    private void AddDataToBill_Detail(){
        DatabaseReference bill_detailRef = FirebaseDatabase.getInstance().getReference().child("Bill_Detail");
        for(Bill_Detail billDetail : bill_detailList){
            String billId = bill_detailRef.push().getKey();
            bill_detailRef.child(billId).setValue(billDetail)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //xóa các mục đã thêm vào hóa đơn khỏi giỏ hàng
                            DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart");
                            cartRef.orderByChild("user_Id").equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                        Cart cartItem = snapshot1.getValue(Cart.class);
                                        if (cartItem != null && cartItem.getId() != null && billDetail.getId() != null && cartItem.getId().equals(billDetail.getId())) {
                                            snapshot1.getRef().removeValue();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(oderDeatil.this, "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        bill_detailList.clear();
    }

    private void AddDataToBill(){
        DatabaseReference billRef = FirebaseDatabase.getInstance().getReference().child("Bill");
        bill_Id = generateBillId();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String purchase_Date = dateFormat.format(calendar.getTime());

        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        String hourString = hourFormat.format(calendar.getTime());

        int totalCost = Integer.parseInt(binding.TotailOderDeatail.getText().toString());
        Bill bill = new Bill(bill_Id, userId, purchase_Date,address,totalCost,0,"...",hourString);
        billRef.child(bill_Id).setValue(bill)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intent = new Intent(oderDeatil.this, Confirmed_order.class);
                        intent.putExtra("bill_Id",bill_Id);
                        intent.putExtra("fullName",fullName);
                        intent.putExtra("purchase_Date",purchase_Date);
                        intent.putExtra("total_Cost",totalCost);
                        intent.putExtra("phoneNumber",phoneNumber);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(oderDeatil.this, "Đặt hàng không thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String generateBillId() {
        if (bill_Id == null || bill_Id.isEmpty()) {
            bill_Id = UUID.randomUUID().toString();
        }
        return bill_Id;
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