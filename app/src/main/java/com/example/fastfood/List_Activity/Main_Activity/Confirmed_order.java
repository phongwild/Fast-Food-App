package com.example.fastfood.List_Activity.Main_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fastfood.Model.Notification;
import com.example.fastfood.R;
import com.example.fastfood.databinding.ActivityConfirmedOrderBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class Confirmed_order extends AppCompatActivity {
    private ActivityConfirmedOrderBinding binding;
    String bill_Id,fullName,purchase_Date,userId;
    private SharedPreferences shareRef;
    int totalCost,phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed_order);
        binding = ActivityConfirmedOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if(intent != null){
            bill_Id = intent.getStringExtra("bill_Id");
            fullName = intent.getStringExtra("fullName");
            phoneNumber = intent.getIntExtra("phoneNumber",0);
            purchase_Date = intent.getStringExtra("purchase_Date");
            totalCost = intent.getIntExtra("total_Cost",0);
        }

        shareRef = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        userId = shareRef.getString("userId","");

        binding.confirmOrderId.setText(bill_Id);
        binding.confirmTotal.setText(String.valueOf(totalCost) + " VNĐ");
        binding.confirmDateTime.setText(purchase_Date);
        binding.confirmName.setText(fullName);
        binding.confirmPhone.setText(String.valueOf("0" + phoneNumber));

        binding.backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Confirmed_order.this, MainActivity_User.class));
                addNotification();
            }
        });
    }

    private void addNotification(){
        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child("Notification").push();
        String notifi_Id = UUID.randomUUID().toString();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        String purchase_Date = dateFormat.format(calendar.getTime());

        Notification notification = new Notification();
        notification.setNotification_Id(notifi_Id);
        notification.setContent_Notification("Đơn hàng " + bill_Id + " đang chờ xác nhận");
        notification.setSend_Date(purchase_Date);
        notification.setUser_Id(userId);

        notificationRef.setValue(notification)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Confirmed_order.this, "add notification lỗi", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}