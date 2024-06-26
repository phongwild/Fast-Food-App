package com.example.fastfood.List_Activity.Activity_Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.fastfood.R;
import com.example.fastfood.databinding.ActivityForgetPasswordBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Forget_Password extends AppCompatActivity {
    private ActivityForgetPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Forget_Password.this, Main_Login.class));
            }
        });
        // thực hiện tìm kiếm sdt và trả về thông báo mật khẩu
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
        binding.NextFogetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.ForgetEmail.getText().toString();

                if(!TextUtils.isEmpty(email)){
                    Query emailQuery = userRef.orderByChild("email").equalTo(email);
                    emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                for (DataSnapshot userSnapshot : snapshot.getChildren()){
                                    String password = userSnapshot.child("pass_Word").getValue(String.class);
                                    if(password != null){
                                        startActivity(new Intent(Forget_Password.this, Main_Login.class));
                                        Toast.makeText(Forget_Password.this, "Mật khẩu của bạn là: " + password, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else{
                                Toast.makeText(Forget_Password.this, "Email không tồn tại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    binding.ForgetEmail.setError("Vui lòng nhập email!");
                }
            }
        });

    }
}