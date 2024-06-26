package com.example.fastfood.List_Activity.Activity_Management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fastfood.Adapter.Product_Adapter;
import com.example.fastfood.List_Activity.Main_Activity.MainActivity_Admin;
import com.example.fastfood.Model.Product;
import com.example.fastfood.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Product_Manager extends AppCompatActivity {
    Context context;
    ImageView btn_back_product;
    RecyclerView rcv_product;
    Product_Adapter adapter;
    FloatingActionButton fab;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_manager);
        btn_back_product = findViewById(R.id.btn_back_product);
        btn_back_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Product_Manager.this, Product_Type_Manager.class);
                startActivity(intent);
                finish();
            }
        });

        rcv_product = findViewById(R.id.rcv_product);
        rcv_product.setLayoutManager(new LinearLayoutManager(this));

        String productTypeId = getIntent().getStringExtra("Product_Type_Id");

        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Product")
                                .orderByChild("Product_Type_Id").equalTo(productTypeId), Product.class)
                        .build();

        adapter = new Product_Adapter(options, this);

        rcv_product.setAdapter(adapter);
        fab = findViewById(R.id.fab_add_product);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog_Add();
            }
        });
        rcv_product.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        //

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    public void showDialog_Add(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_product);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.style_dialog);
        TextInputEditText id, name, img, describe, price, type_id;
        Button btn_add;
        id = dialog.findViewById(R.id.dialog_id_product);
        name = dialog.findViewById(R.id.dialog_name_product);
        describe = dialog.findViewById(R.id.dialog_describe_product);
        price = dialog.findViewById(R.id.dialog_price_product);
        img = dialog.findViewById(R.id.dialog_img_product);
        type_id = dialog.findViewById(R.id.dialog_typeId_product);
        btn_add = dialog.findViewById(R.id.dialog_btn_add);
        String productTypeId = getIntent().getStringExtra("Product_Type_Id");
        type_id.setText(productTypeId);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data_id = id.getText().toString().trim();
                String data_name = name.getText().toString().trim();
                String data_describe = describe.getText().toString().trim();
                String data_price = price.getText().toString().trim(); // Sử dụng String thay vì int để kiểm tra rỗng
                String data_img = img.getText().toString().trim();
                String data_typeId = type_id.getText().toString().trim();

                // Kiểm tra nếu bất kỳ trường nào rỗng, không thêm sản phẩm
                if (TextUtils.isEmpty(data_id) || TextUtils.isEmpty(data_name) ||
                        TextUtils.isEmpty(data_describe) || TextUtils.isEmpty(data_price) ||
                        TextUtils.isEmpty(data_img) || TextUtils.isEmpty(data_typeId)) {

                    Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    // Nếu các trường không rỗng, thực hiện thêm sản phẩm vào cơ sở dữ liệu Firebase
                    int intPrice = Integer.parseInt(data_price);

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product");
                    String key = databaseReference.push().getKey();
                    databaseReference.child(key).child("id").setValue(data_id);
                    databaseReference.child(key).child("Describe").setValue(data_describe);
                    databaseReference.child(key).child("Img_Product").setValue(data_img);
                    databaseReference.child(key).child("Name").setValue(data_name);
                    databaseReference.child(key).child("Price").setValue(intPrice);
                    databaseReference.child(key).child("Product_Type_Id").setValue(data_typeId);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

}