package com.example.fastfood.List_Activity.Activity_Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fastfood.List_Activity.Main_Activity.MainActivity_User;
import com.example.fastfood.R;
import com.example.fastfood.databinding.ActivityConfirmBinding;

public class Confirm extends AppCompatActivity {
    private ActivityConfirmBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        binding = ActivityConfirmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.TryOderConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Confirm.this, Main_Login.class));
            }
        });
    }
}