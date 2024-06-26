package com.example.fastfood.List_Activity.Onboarding_Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.fastfood.Adapter.Onboarding_Adapter;
import com.example.fastfood.List_Activity.Activity_Login.Main_Login;
import com.example.fastfood.R;
import com.example.fastfood.databinding.ActivityMainOnboardingBinding;

public class Main_Onboarding extends AppCompatActivity {
    private ActivityMainOnboardingBinding binding;
    Onboarding_Adapter adapter;
    private static final String PREF_NAME = "onboardingPrefs";
    private static final String KEY_ONBOARDING_COMPLETED = "isOnboardingCompleted";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // thực hiện khi tải app về chỉ thực hiện 1 lần duy nhất

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean onboardingCompleted = sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED, false);
        if(onboardingCompleted){
            startActivity(new Intent(Main_Onboarding.this, Main_Login.class));
        }else{
            // nếu chưa hoàn thành hiển thị màn hình onboarding
            setContentView(R.layout.activity_main_onboarding);
            // các code xử lú onboarding
            binding = ActivityMainOnboardingBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            adapter = new Onboarding_Adapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            binding.viewPagerOnbroarding.setAdapter(adapter);

            binding.OnbroardingSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.viewPagerOnbroarding.setCurrentItem(2);
                }
            });

            binding.OnbroardingNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentPage = binding.viewPagerOnbroarding.getCurrentItem();
                    if (currentPage < adapter.getCount() - 1) {
                        binding.viewPagerOnbroarding.setCurrentItem(currentPage + 1);
                    }
                }
            });


            binding.viewPagerOnbroarding.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if(position == 2){
                        binding.OnbroardingSkip.setVisibility(View.GONE);
                        binding.OnbroardingNext.setVisibility(View.GONE);
                    }else{
                        binding.OnbroardingSkip.setVisibility(View.VISIBLE);
                        binding.OnbroardingNext.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            // lưu trạng thái đã hoàn thành
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(KEY_ONBOARDING_COMPLETED, true);
            editor.apply();
        }
    }
}