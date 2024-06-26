package com.example.fastfood.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fastfood.Fragment.Fragment_Onbrarding.Fragment_Onbrarding1;
import com.example.fastfood.Fragment.Fragment_Onbrarding.Fragment_Onbrarding2;
import com.example.fastfood.Fragment.Fragment_Onbrarding.Fragment_Onbrarding3;

public class Onboarding_Adapter extends FragmentStatePagerAdapter {
    public Onboarding_Adapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Fragment_Onbrarding1();
            case 1:
                return new Fragment_Onbrarding2();
            case 2:
                return new Fragment_Onbrarding3();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
