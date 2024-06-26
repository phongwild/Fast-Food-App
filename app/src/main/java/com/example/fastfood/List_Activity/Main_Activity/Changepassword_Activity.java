package com.example.fastfood.List_Activity.Main_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.fastfood.Fragment.Fragment_Admin.Fragment_Admin_Personal_Information;
import com.example.fastfood.R;
import com.example.fastfood.databinding.ActivityChangepasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Changepassword_Activity extends AppCompatActivity {
    private ActivityChangepasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        binding = ActivityChangepasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backChangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(Changepassword_Activity.this, MainActivity_User.class);
                //startActivity(intent);
                finish();
            }
        });
        binding.btnChangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }
    public void validate(){
        String oldPass = binding.edOldPassWord.getText().toString().trim();
        String newPass = binding.edNewPassWord.getText().toString().trim();
        String reNewPass = binding.edRePassWord.getText().toString().trim();
        if (oldPass.isEmpty()) {
            binding.inOldPassWord.setError("Không để trống oldPassword");
        }else  binding.inOldPassWord.setError(null);

        if (newPass.isEmpty()) {
            binding.inNewPassWord.setError("Không để trống newPassword");
        }else  binding.inNewPassWord.setError(null);

        if (reNewPass.isEmpty()) {
            binding.inRePassWord.setError("Không để trống reNewPassword");
        }else  binding.inRePassWord.setError(null);

        if (!oldPass.isEmpty() && !newPass.isEmpty() && !reNewPass.isEmpty()) {
            if (reNewPass.equals(newPass)) {
                changePass();

            } else {
                binding.inRePassWord.setError("RePass phải trùng với new password");
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
                        String old_Pass = binding.edOldPassWord.getText().toString();
                        String new_Pass = binding.edNewPassWord.getText().toString();

                        //Ktra pass
                        if (old_Pass.equals(oldPass_RD)) {
                            userRef.child(userId).child("pass_Word").setValue(new_Pass);
                            Toast.makeText(Changepassword_Activity.this,"Change pass success", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Changepassword_Activity.this, MainActivity_User.class));
                        } else {
                            Toast.makeText(Changepassword_Activity.this,"Old password incorrect", Toast.LENGTH_SHORT).show();
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