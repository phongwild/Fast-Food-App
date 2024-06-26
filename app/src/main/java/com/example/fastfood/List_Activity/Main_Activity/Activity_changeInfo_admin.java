package com.example.fastfood.List_Activity.Main_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fastfood.R;
import com.example.fastfood.databinding.ActivityChangeInfoAdminBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_changeInfo_admin extends AppCompatActivity {
    private ActivityChangeInfoAdminBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info_admin);
        binding = ActivityChangeInfoAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnBackInfoAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        String savedUsername = getUsernameFromSharedPreferences();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
        userRef.orderByChild("user_Name").equalTo(savedUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        String userId = snapshot1.getKey();
                        String name = snapshot1.child("full_Name").getValue(String.class);
                        binding.edFullNameChangeAdmin.setText(name);
                        String userName = snapshot1.child("user_Name").getValue(String.class);
                        binding.edUserNameChangeAdmin.setText(userName);
                        String email = snapshot1.child("email").getValue(String.class);
                        binding.edEmailChangeAdmin.setText(email);
                        String phoneNumber = String.valueOf(snapshot1.child("phone_Number").getValue(Integer.class));
                        binding.edPhoneNumberChangeAdmin.setText(phoneNumber);

                        String address = snapshot1.child("address").getValue(String.class);
                        binding.edAddRessChangeAdmin.setText(address);

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.btnSaveChangeAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeInfo();
                finish();
            }
        });
    }
    public void changeInfo(){
        String savedUsername = getUsernameFromSharedPreferences();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
        userRef.orderByChild("user_Name").equalTo(savedUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        // Lấy thông tin user
                        String userId = snapshot1.getKey();

                        //lấy dl từ edittext
                        String fullName = binding.edFullNameChangeAdmin.getText().toString().trim();
                        String userName = binding.edUserNameChangeAdmin.getText().toString().trim();
                        String email = binding.edEmailChangeAdmin.getText().toString().trim();
                        int phoneNumber = Integer.parseInt(binding.edPhoneNumberChangeAdmin.getText().toString().trim());
                        String address = binding.edAddRessChangeAdmin.getText().toString().trim();

                        //thực hiện đổi thông tin
                        userRef.child(userId).child("full_Name").setValue(fullName);
                        userRef.child(userId).child("user_Name").setValue(userName);
                        userRef.child(userId).child("email").setValue(email);
                        userRef.child(userId).child("phone_Number").setValue(phoneNumber);
                        userRef.child(userId).child("address").setValue(address);
                        Toast.makeText(getApplicationContext(),"Change success", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("loginStatus", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("username", ""); // "" là giá trị mặc định nếu không tìm thấy username trong SharedPreferences
    }
}