package com.example.fastfood.List_Activity.Activity_Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.fastfood.Model.User;
import com.example.fastfood.R;
import com.example.fastfood.databinding.ActivityEnterLocationBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;


public class Enter_Location extends AppCompatActivity {
    private ActivityEnterLocationBinding binding;
    String username, password, fullname, phonenumberStr, email;
    int phonenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_location);
        binding = ActivityEnterLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backEnterLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Enter_Location.this, Enter_Information.class));
            }
        });
        binding.NextLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = binding.setLocation.getText().toString();

                if(location.isEmpty()){
                    if(location.equals("")){
                        binding.setLocation.setError("Vui lòng nhập đầy đủ địa chỉ");
                    }
                    if(location.length() < 10){
                        binding.setLocation.setError("Địa chỉ phải nhập 10 ký tự trở lên");
                    }
                }else{
                    Bundle bundle = getIntent().getExtras();
                    if(bundle != null){
                         username = bundle.getString("Username");
                         password = bundle.getString("Password");
                         fullname = bundle.getString("Fullname");
                         phonenumberStr = bundle.getString("Phonenumber", "");
                         phonenumber = Integer.parseInt(phonenumberStr);
                         email = bundle.getString("email");
                    }
                    String UserImg = "https://icons.veryicon.com/png/o/internet--web/prejudice/user-128.png";
                    String userId = UUID.randomUUID().toString();
                    int Role = 1;

                    User newUser = new User(userId, username, password, Role, email, fullname, location, phonenumber, UserImg);

                    // Thêm mới user vào firebase hoàn thiện đăng nhập
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
                    userRef.child(userId).setValue(newUser)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    startActivity(new Intent(Enter_Location.this, Confirm.class));
                                    Toast.makeText(Enter_Location.this, "Đăng ký " + fullname + " thành công", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Enter_Location.this, "Dăng ký " + fullname + " không thành công", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }
}