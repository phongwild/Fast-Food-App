package com.example.fastfood.List_Activity.Activity_Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;

import com.example.fastfood.Adapter.Login_Adapter;
import com.example.fastfood.Fragment.Fragment_Login_Account.Fragment_Create_Account;
import com.example.fastfood.Fragment.Fragment_Login_Account.Fragment_Login;
import com.example.fastfood.R;
import com.example.fastfood.databinding.ActivityMainLoginBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Main_Login extends AppCompatActivity {
    private ActivityMainLoginBinding binding;
    float v = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        configTabLayout();

        binding.tabLayoutLogin.setTranslationY(300);
        binding.tabLayoutLogin.setAlpha(v);
        binding.tabLayoutLogin.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(800).start();
    }

    private void configTabLayout(){
        Login_Adapter adapter = new Login_Adapter(this);
        binding.viewPagerLogin.setAdapter(adapter);
        adapter.addFragment(new Fragment_Login(), "Login");
        adapter.addFragment(new Fragment_Create_Account(), "Create Account");

        TabLayoutMediator mediator = new TabLayoutMediator(binding.tabLayoutLogin, binding.viewPagerLogin, (tab, position) -> {
            tab.setText(adapter.getTitle(position));
        });
        mediator.attach();
    }
}