package com.example.fastfood.List_Activity.Activity_Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.fastfood.R;
import com.example.fastfood.databinding.ActivityEnterInformationBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Enter_Information extends AppCompatActivity {
    private ActivityEnterInformationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_information);
        binding = ActivityEnterInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backEnterInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Enter_Information.this, Main_Login.class));
            }
        });

        binding.NextEnterInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname = binding.FullNameEnterInformation.getText().toString();
                String phonenumber = binding.PhoneNumberEnterInformation.getText().toString();
                String email = binding.EmailEnterInformation.getText().toString();

                if(fullname.isEmpty()) {
                    binding.FullNameEnterInformation.setError("Vui lòng điền đầy đủ họ tên");
                } else {
                    binding.FullNameEnterInformation.setError(null);
                }

                if(phonenumber.isEmpty()) {
                    binding.PhoneNumberEnterInformation.setError("Vui lòng điền số điện thoại");
                } else if (!isValidPhoneNumber(phonenumber)) {
                    binding.PhoneNumberEnterInformation.setError("Số điện thoại không hợp lệ");
                } else {
                    binding.PhoneNumberEnterInformation.setError(null);
                }

                if(email.isEmpty()){
                    binding.EmailEnterInformation.setError("Vui lòng không để trống email!");
                } else if (!isValidEmail(email)) {
                    binding.EmailEnterInformation.setError("Email không hợp lệ");
                } else {
                    binding.EmailEnterInformation.setError(null);
                }

                if (!fullname.isEmpty() && isValidPhoneNumber(phonenumber) && isValidEmail(email)) {
                    // Nếu cả hai trường đều không rỗng và số điện thoại hợp lệ, chuyển sang màn hình tiếp theo
                    Intent intent = new Intent(Enter_Information.this, Enter_Location.class);
                    Bundle receivedBundle = getIntent().getExtras();
                    Bundle newBundle = new Bundle();
                    if(receivedBundle != null){
                        newBundle.putAll(receivedBundle);
                    }
                    newBundle.putString("Fullname", fullname);
                    newBundle.putString("Phonenumber", phonenumber);
                    newBundle.putString("email", email);
                    intent.putExtras(newBundle);
                    startActivity(intent);
                }
            }
        });

    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Định nghĩa biểu thức chính quy cho định dạng số điện thoại
        String phoneRegex = "^[+]?[0-9]{10,13}$"; // Định dạng có thể thay đổi tùy theo yêu cầu

        // Khớp chuỗi với biểu thức chính quy
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.matches();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"; // Biểu thức chính quy kiểm tra email

        // Khớp chuỗi với biểu thức chính quy
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}