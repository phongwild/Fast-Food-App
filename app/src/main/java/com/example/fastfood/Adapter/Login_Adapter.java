package com.example.fastfood.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fastfood.Fragment.Fragment_Login_Account.Fragment_Create_Account;
import com.example.fastfood.Fragment.Fragment_Login_Account.Fragment_Login;

import java.util.ArrayList;

public class Login_Adapter extends FragmentStateAdapter {

    private ArrayList<Fragment> frglist = new ArrayList<>();
    private ArrayList<String> titlelist = new ArrayList<>();

    public Login_Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public String getTitle(int position){
        return titlelist.get(position);
    }

    public void addFragment(Fragment fragment, String title){
        frglist.add(fragment);
        titlelist.add(title);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return frglist.get(position);
    }

    @Override
    public int getItemCount() {
        return frglist.size();
    }


}
