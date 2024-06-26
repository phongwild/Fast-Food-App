package com.example.fastfood.List_Activity.Main_Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.example.fastfood.Model.Bill;
import com.example.fastfood.R;
import com.example.fastfood.databinding.ActivityStatisticalBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Statistical_Activity extends AppCompatActivity {
    private ActivityStatisticalBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        updateTotalRevenueForCurrentDay();

        binding.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy ngày hiện tại
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Tạo DatePickerDialog để chọn tháng
                DatePickerDialog datePickerDialog = new DatePickerDialog(Statistical_Activity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                filterDataByMonth(year, monthOfYear + 1); // Tháng trong Calendar từ 0-11, cần +1 để chuyển sang 1-12
                            }
                        }, year, month, dayOfMonth);

                // Hiển thị DatePickerDialog
                datePickerDialog.show();
            }
        });

        binding.btnBackStatistical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
    }

    private void updateTotalRevenueForCurrentDay() {

        FirebaseDatabase.getInstance().getReference().child("Bill")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        double totalRevenue = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Bill bill = dataSnapshot.getValue(Bill.class);
                            if (bill != null && bill.getStatus() == 2) {
                                totalRevenue += bill.getTotal_Amount();
                            }
                        }
                        // Hiển thị tổng doanh thu trên giao diện
                        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                        String formattedTotalRevenue = formatter.format(totalRevenue);
                        binding.total.setText(String.valueOf(formattedTotalRevenue));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý khi có lỗi xảy ra
                    }
                });
    }

    private void filterDataByMonth(int year, int month) {
        FirebaseDatabase.getInstance().getReference().child("Bill")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        double totalRevenue = 0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Bill bill = dataSnapshot.getValue(Bill.class);
                            if (bill != null && bill.getStatus() == 2) {
                                // Lấy ngày mua hàng từ đối tượng Bill
                                String purchaseDate = bill.getPurchase_Date();

                                // Chuyển đổi chuỗi ngày tháng sang Date
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                try {
                                    java.util.Date date = dateFormat.parse(purchaseDate);

                                    // Kiểm tra tháng và năm
                                    Calendar billCalendar = Calendar.getInstance();
                                    billCalendar.setTime(date);

                                    int billYear = billCalendar.get(Calendar.YEAR);
                                    int billMonth = billCalendar.get(Calendar.MONTH) + 1; // Chuyển từ 0-11 sang 1-12

                                    if (billYear == year && billMonth == month) {
                                        totalRevenue += bill.getTotal_Amount();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        // Hiển thị tổng doanh thu của tháng trên giao diện
                        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                        String formattedTotalRevenue = formatter.format(totalRevenue);
                        binding.total.setText(String.valueOf(formattedTotalRevenue));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý khi có lỗi xảy ra
                    }
                });
    }


}