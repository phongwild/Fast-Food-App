package com.example.fastfood.List_Activity.Activity_Management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fastfood.Adapter.User_Adapter;
import com.example.fastfood.List_Activity.Main_Activity.MainActivity_Admin;
import com.example.fastfood.Model.User;
import com.example.fastfood.R;
import com.example.fastfood.databinding.ActivityEmployeeManagerBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.UUID;

public class User_Manager extends AppCompatActivity {
    private ActivityEmployeeManagerBinding binding;
    User_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_manager);
        binding = ActivityEmployeeManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.rcvUserManager.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("User"), User.class)
                        .build();
        adapter = new User_Adapter(options,this);
        binding.rcvUserManager.setAdapter(adapter);

        binding.addUserManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialogAdd();
            }
        });

        binding.backUserManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(User_Manager.this, MainActivity_Admin.class));
            }
        });
    }

    private void showdialogAdd(){
        final DialogPlus dialogPlus = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.add_user))
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT) // Đặt chiều rộng của nội dung
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT) // Đặt chiều cao của nội dung
                .create();

        View view = dialogPlus.getHolderView();

        EditText update_Account_User = view.findViewById(R.id.update_Account_User);
        EditText update_PassWord_User = view.findViewById(R.id.update_PassWord_User);
        EditText update_Role_User = view.findViewById(R.id.update_Role_User);
        EditText update_Email_User = view.findViewById(R.id.update_Email_User);
        EditText update_FullName_User = view.findViewById(R.id.update_FullName_User);
        EditText update_Address_User = view.findViewById(R.id.update_Address_User);
        EditText update_PhoneNumber_User = view.findViewById(R.id.update_PhoneNumber_User);
        Button btnadd = view.findViewById(R.id.update_User);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy giá trị từ các EditText
                String UserAccount = update_Account_User.getText().toString().trim();
                String UserPassword = update_PassWord_User.getText().toString().trim();
                String UserRoleStr = update_Role_User.getText().toString().trim();
                String UserEmail = update_Email_User.getText().toString().trim();
                String UserFullName = update_FullName_User.getText().toString().trim();
                String UserAddress = update_Address_User.getText().toString().trim();
                String UserPhoneNumberStr = update_PhoneNumber_User.getText().toString().trim();

                // Kiểm tra không để trống và hiển thị lỗi
                if (UserAccount.isEmpty() || UserPassword.isEmpty() || UserRoleStr.isEmpty() || UserEmail.isEmpty() || UserFullName.isEmpty() || UserAddress.isEmpty() || UserPhoneNumberStr.isEmpty()) {
                    if (UserAccount.isEmpty()) {
                        update_Account_User.setError("Vui lòng nhập tên tài khoản");
                        return;
                    }

                    if (UserPassword.isEmpty()) {
                        update_PassWord_User.setError("Vui lòng nhập mật khẩu");
                        return;
                    }

                    if (UserRoleStr.isEmpty()) {
                        update_Role_User.setError("Vui lòng nhập vai trò");
                        return;
                    }

                    if (UserEmail.isEmpty()) {
                        update_Email_User.setError("Vui lòng nhập địa chỉ email");
                        return;
                    }

                    if (UserFullName.isEmpty()) {
                        update_FullName_User.setError("Vui lòng nhập họ và tên");
                        return;
                    }

                    if (UserAddress.isEmpty()) {
                        update_Address_User.setError("Vui lòng nhập địa chỉ");
                        return;
                    }

                    if (UserPhoneNumberStr.isEmpty()) {
                        update_PhoneNumber_User.setError("Vui lòng nhập số điện thoại");
                        return;
                    }
                }

                if (UserPassword.length() < 10) {
                    update_PassWord_User.setError("Mật khẩu phải có ít nhất 10 ký tự");
                    return;
                }

                // Kiểm tra Role là số và hiển thị lỗi
                int UserRole;
                try {
                    UserRole = Integer.parseInt(UserRoleStr);
                    if (UserRole < 0 || UserRole > 1) {
                        update_Role_User.setError("Role phải nằm trong khoảng từ 0 đến 1");
                        return;
                    }
                } catch (NumberFormatException e) {
                    update_Role_User.setError("Role phải là số");
                    return;
                }

                // Kiểm tra PhoneNumber là số và đúng định dạng, hiển thị lỗi
                long UserPhoneNumber;
                try {
                    UserPhoneNumber = Long.parseLong(UserPhoneNumberStr);
                    if (UserPhoneNumber < 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    update_PhoneNumber_User.setError("PhoneNumber không hợp lệ");
                    return;
                }

                // Kiểm tra Email đúng định dạng, hiển thị lỗi
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(UserEmail).matches()) {
                    update_Email_User.setError("Email không đúng định dạng");
                    return;
                }

                // Tạo mới User nếu thông tin hợp lệ
                String UserImg = "https://icons.veryicon.com/png/o/internet--web/prejudice/user-128.png";
                String userId = UUID.randomUUID().toString();

                User newUser = new User(userId, UserAccount, UserPassword, UserRole, UserEmail, UserFullName, UserAddress, (int) UserPhoneNumber, UserImg);

                // Thêm User mới vào Firebase
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("User");
                usersRef.child(userId).setValue(newUser)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(User_Manager.this, "Thêm " + UserFullName + " thành công", Toast.LENGTH_SHORT).show();
                                dialogPlus.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(User_Manager.this, "Thêm " + UserFullName + " thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


        dialogPlus.show();
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