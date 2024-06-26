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
import com.example.fastfood.databinding.ActivityChangePassAdminBinding;

import com.example.fastfood.databinding.ActivityChangePassAdminBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePass_Activity_Admin extends AppCompatActivity {
    private ActivityChangePassAdminBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_admin);
        binding = ActivityChangePassAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backChangepasswordAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.btnChangepasswordAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }
    public void validate(){
        String oldPass = binding.edOldPassWordAd.getText().toString().trim();
        String newPass = binding.edNewPassWordAd.getText().toString().trim();
        String reNewPass = binding.edRePassWordAd.getText().toString().trim();
        if (oldPass.isEmpty()) {
            binding.inOldPassWordAd.setError("Không để trống oldPassword");
        }else  binding.inOldPassWordAd.setError(null);

        if (newPass.isEmpty()) {
            binding.inNewPassWordAd.setError("Không để trống newPassword");
        }else  binding.inNewPassWordAd.setError(null);

        if (reNewPass.isEmpty()) {
            binding.inRePassWordAd.setError("Không để trống reNewPassword");
        }else  binding.inRePassWordAd.setError(null);

        if (!oldPass.isEmpty() && !newPass.isEmpty() && !reNewPass.isEmpty()) {
            if (reNewPass.equals(newPass)) {
                changePass();
            } else {
                binding.inRePassWordAd.setError("RePass phải trùng với new password");
            }
        }
    }
    public void changePass(){
        String savedUsername = getUsernameFromSharedPreferences();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
        userRef.orderByChild("user_Name").equalTo(savedUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        // Lấy thông tin user
                        String userId = snapshot1.getKey();
                        String oldPass_RD = snapshot1.child("pass_Word").getValue(String.class);

                        //lấy mk mũ và mới
                        String old_Pass = binding.edOldPassWordAd.getText().toString();
                        String new_Pass = binding.edNewPassWordAd.getText().toString();

                        //Ktra pass
                        if (old_Pass.equals(oldPass_RD)) {
                            userRef.child(userId).child("pass_Word").setValue(new_Pass);
                            Toast.makeText(ChangePass_Activity_Admin.this,"Change pass success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ChangePass_Activity_Admin.this, MainActivity_Admin.class));
                        } else {
                            Toast.makeText(ChangePass_Activity_Admin.this,"Old password incorrect", Toast.LENGTH_SHORT).show();
                        }
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