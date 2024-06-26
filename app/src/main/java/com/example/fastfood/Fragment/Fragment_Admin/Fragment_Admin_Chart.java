package com.example.fastfood.Fragment.Fragment_Admin;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fastfood.Adapter.TopProduct_Adapter;
import com.example.fastfood.Model.Bill;
import com.example.fastfood.Model.Bill_Detail;
import com.example.fastfood.Model.Product;
import com.example.fastfood.R;
import com.example.fastfood.databinding.FragmentAdminBillBinding;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

public class Fragment_Admin_Chart extends Fragment {
    public Fragment_Admin_Chart() {

    }

    private FragmentAdminBillBinding binding;
    List<Bill_Detail> billDetails = new ArrayList<>();
    private PieChart pieChart;
    private boolean dataLoaded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminBillBinding.inflate(inflater,container,false);
        View view = binding.getRoot();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.TopProduct.setLayoutManager(layoutManager);

        // Truy vấn Firebase để lấy dữ liệu Bill với status = 2
        Query billQuery = FirebaseDatabase.getInstance().getReference().child("Bill").orderByChild("status").equalTo(2);

        billQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot billSnapshot : dataSnapshot.getChildren()) {
                        // Lấy thông tin của mỗi Bill có status = 2
                        Bill bill = billSnapshot.getValue(Bill.class);
                        String billId = bill.getBill_Id();
                        Log.d("Bill_Id", billId); // Kiểm tra xem bạn đã nhận đúng Bill_Id không

                        // Truy vấn Bill_Detail của Bill có status = 2
                        Query billDetailQuery = FirebaseDatabase.getInstance().getReference().child("Bill_Detail").orderByChild("bill_Id").equalTo(billId);

                        billDetailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Map<String, Bill_Detail> productMap = new HashMap<>();

                                    // Gộp dữ liệu từ Firebase
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        Bill_Detail billDetail = snapshot.getValue(Bill_Detail.class);
                                        String productId = billDetail.getId();
                                        Log.d("Product_Id", productId);

                                        boolean isExistingProduct = false;

                                        // Kiểm tra xem sản phẩm đã tồn tại trong danh sách chưa
                                        for (Bill_Detail existingProduct : billDetails) {
                                            if (existingProduct.getId().equals(productId)) {
                                                // Nếu sản phẩm đã tồn tại, cộng dồn cart_quantity
                                                existingProduct.setCart_quantity(existingProduct.getCart_quantity() + billDetail.getCart_quantity());
                                                existingProduct.setTotal_details(existingProduct.getTotal_details() + billDetail.getTotal_details());
                                                isExistingProduct = true;
                                                break;
                                            }
                                        }

                                        if (!isExistingProduct) {
                                            // Nếu sản phẩm chưa tồn tại, thêm mới vào danh sách
                                            billDetails.add(billDetail);
                                        }
                                    }

                                    // Sắp xếp danh sách theo số lượng giảm dần
                                    Collections.sort(billDetails, (bd1, bd2) -> {
                                        return bd2.getCart_quantity() - bd1.getCart_quantity();
                                    });

                                    // Giới hạn danh sách chỉ có 5 sản phẩm đầu tiên
                                    if (billDetails.size() > 5) {
                                        billDetails = billDetails.subList(0, 5);
                                    }

                                    // Khởi tạo adapter mới với danh sách đã giới hạn
                                    RecyclerView recyclerView = view.findViewById(R.id.TopProduct);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                                    recyclerView.setLayoutManager(layoutManager);

                                    TopProduct_Adapter adapter = new TopProduct_Adapter(billDetails);
                                    recyclerView.setAdapter(adapter);

                                    drawPieChart(); // Gọi phương thức vẽ biểu đồ sau khi dữ liệu đã được cập nhật
                                    dataLoaded = true;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Xử lý khi có lỗi
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi
            }
        });

        //chart
        pieChart = binding.pieChart;

        return view;
    }

    // Phương thức để vẽ biểu đồ PieChart với dữ liệu từ danh sách billDetails
    private void drawPieChart() {
        // Truy vấn Firebase để lấy dữ liệu Product
        Query productQuery = FirebaseDatabase.getInstance().getReference().child("Product");

        // Lắng nghe dữ liệu từ Firebase
        productQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, String> productNames = new HashMap<>();

                    // Lặp qua dữ liệu từ bảng Product để lấy thông tin tên sản phẩm
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Product product = snapshot.getValue(Product.class);
                        productNames.put(product.getId(), product.getName());
                    }

                    // Sau khi đã có thông tin tên sản phẩm, gán vào biểu đồ PieChart
                    List<PieEntry> entries = new ArrayList<>();
                    for (Bill_Detail billDetail : billDetails) {
                        String productName = productNames.get(billDetail.getId());
                        if (productName != null) {
                            entries.add(new PieEntry(billDetail.getTotal_details(), productName));
                        }
                    }

                    // Vẽ biểu đồ PieChart
                    PieDataSet dataSet = new PieDataSet(entries, "Sales");
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                    PieData pieData = new PieData(dataSet);
                    pieChart.setData(pieData);
                    pieChart.animateXY(1000, 1000);
                    pieChart.setEntryLabelColor(Color.BLACK);
                    pieChart.setEntryLabelTextSize(12f);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Vẽ biểu đồ khi danh sách billDetails có dữ liệu
        if (!billDetails.isEmpty()) {
            drawPieChart();
        }
    }
}