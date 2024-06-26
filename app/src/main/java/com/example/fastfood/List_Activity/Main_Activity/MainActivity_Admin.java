package com.example.fastfood.List_Activity.Main_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.fastfood.Adapter.Menu_Admin_Adapter;
import com.example.fastfood.Fragment.Fragment_Admin.Fragment_Admin_Home;
import com.example.fastfood.Fragment.Fragment_Admin.Fragment_Admin_Chart;
import com.example.fastfood.Fragment.Fragment_Admin.Fragment_Admin_Notification;
import com.example.fastfood.Fragment.Fragment_Admin.Fragment_Admin_Personal_Information;
import com.example.fastfood.List_Activity.Activity_Management.Bill_Manager;
import com.example.fastfood.List_Activity.Activity_Management.Product_Type_Manager;
import com.example.fastfood.List_Activity.Activity_Management.User_Manager;
import com.example.fastfood.List_Activity.Activity_Management.Product_Manager;
import com.example.fastfood.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity_Admin extends AppCompatActivity {
    BottomNavigationView view;
    Menu_Admin_Adapter adapter;
    ViewPager2 pager2;
    ArrayList<Fragment> list = new ArrayList<>();
    FloatingActionButton manage, employee, bill, product;
    Animation fabOpen, fabClose, rotateOpen, rotateClose;
    boolean isOpen = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        view = findViewById(R.id.bottomNavigationView_admin);
        pager2 = findViewById(R.id.pagerMain_Admin);

        manage = (FloatingActionButton) findViewById(R.id.manage);
        employee = (FloatingActionButton) findViewById(R.id.employee_manager);
        product = (FloatingActionButton) findViewById(R.id.product_manager);
        bill = (FloatingActionButton) findViewById(R.id.bill_manager);

        fabOpen = AnimationUtils.loadAnimation(this, R.anim.open_fab);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.close_fab);
        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_fab);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_fab);

        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity_Admin.this, Product_Type_Manager.class));
            }
        });

        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity_Admin.this, Bill_Manager.class));
            }
        });

        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity_Admin.this, User_Manager.class));
            }
        });

        view.setBackground(null);
        initView();
    }

    private void animateFab(){
        if(isOpen){
            manage.startAnimation(rotateClose);
            employee.startAnimation(fabClose);
            product.startAnimation(fabClose);
            bill.startAnimation(fabClose);
            employee.setClickable(false);
            product.setClickable(false);
            bill.setClickable(false);
            isOpen = false;
        }else{
            manage.startAnimation(rotateOpen);
            employee.startAnimation(fabOpen);
            product.startAnimation(fabOpen);
            bill.startAnimation(fabOpen);
            employee.setClickable(true);
            product.setClickable(true);
            bill.setClickable(true);
            isOpen = true;
        }
    }

    private void initView(){
        list.add(new Fragment_Admin_Home());
        list.add(new Fragment_Admin_Chart());
        list.add(new Fragment_Admin_Notification());
        list.add(new Fragment_Admin_Personal_Information());

        adapter = new Menu_Admin_Adapter(this,list);
        pager2.setAdapter(adapter);
        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        view.setSelectedItemId(R.id.item_admin_home);
                        break;
                    case 1:
                        view.setSelectedItemId(R.id.item_admin_chart);
                        break;
                    case 2:
                        view.setSelectedItemId(R.id.item_admin_notification);
                        break;
                    case 3:
                        view.setSelectedItemId(R.id.item_admin_person);
                        break;
                }
                super.onPageSelected(position);
            }
        });

        view.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.item_admin_home){
                    pager2.setCurrentItem(0);
                }
                if(item.getItemId() == R.id.item_admin_chart){
                    pager2.setCurrentItem(1);
                }
                if(item.getItemId() == R.id.item_admin_notification){
                    pager2.setCurrentItem(2);
                }
                if(item.getItemId() == R.id.item_admin_person){
                    pager2.setCurrentItem(3);
                }
                return true;
            }
        });
    }

}