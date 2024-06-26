package com.example.fastfood.List_Activity.Main_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.fastfood.Adapter.Menu_User_Adapter;
import com.example.fastfood.Fragment.Fragment_User.Fragment_User_Cart;
import com.example.fastfood.Fragment.Fragment_User.Fragment_User_Main;
import com.example.fastfood.Fragment.Fragment_User.Fragment_User_Notification;
import com.example.fastfood.Fragment.Fragment_User.Fragment_User_Personal_Information;
import com.example.fastfood.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity_User extends AppCompatActivity {
    BottomNavigationView view;
    Menu_User_Adapter adapter;
    ViewPager2 pager2;
    ArrayList<Fragment> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.bottom_nav);
        pager2 = findViewById(R.id.pagerMain);
        initView();
    }

    private void initView() {
        list.add(new Fragment_User_Main());
        list.add(new Fragment_User_Cart());
        list.add(new Fragment_User_Notification());
        list.add(new Fragment_User_Personal_Information());
        adapter = new Menu_User_Adapter(this, list);
        pager2.setAdapter(adapter);
        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        view.setSelectedItemId(R.id.item_user_home);
                        break;
                    case 1:
                        view.setSelectedItemId(R.id.item_user_cart);
                        break;
                    case 2:
                        view.setSelectedItemId(R.id.item_user_notification);
                        break;
                    case 3:
                        view.setSelectedItemId(R.id.item_user_person);
                        break;
                }
                super.onPageSelected(position);
            }
        });
        view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.item_user_home){
                    pager2.setCurrentItem(0);
                }
                if(item.getItemId() == R.id.item_user_cart){
                    pager2.setCurrentItem(1);
                }
                if(item.getItemId() == R.id.item_user_notification){
                    pager2.setCurrentItem(2);
                }
                if(item.getItemId() == R.id.item_user_person){
                    pager2.setCurrentItem(3);
                }
                return true;
            }
        });
    }
}