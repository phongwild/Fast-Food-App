package com.example.fastfood.List_Activity.Activity_Management;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fastfood.Adapter.Product_Type_Adapter;
import com.example.fastfood.List_Activity.Main_Activity.MainActivity_Admin;
import com.example.fastfood.Model.ProductType;
import com.example.fastfood.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Product_Type_Manager extends AppCompatActivity {

    Product_Type_Adapter adapter;
    RecyclerView rcv;
    ImageView btn_back;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_type_manager);
        btn_back = findViewById(R.id.btn_back_product_type);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Product_Type_Manager.this, MainActivity_Admin.class);
                startActivity(intent);
                finish();
            }
        });
        rcv = findViewById(R.id.rcv_product_type);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<ProductType> options = new FirebaseRecyclerOptions.Builder<ProductType>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Product_Type"), ProductType.class)
                .build();
        adapter = new Product_Type_Adapter(options);
        rcv.setAdapter(adapter);
        fab = findViewById(R.id.fab_add_product_type);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog_Add();
            }
        });
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
        dialog.setContentView(R.layout.dialog_add_product_type);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.style_dialog);
        TextInputEditText id, name, img;
        Button btn_add;
        id = dialog.findViewById(R.id.dialog_id_product_type);
        name = dialog.findViewById(R.id.dialog_name_product_type);
        img = dialog.findViewById(R.id.dialog_img_product_type);
        btn_add = dialog.findViewById(R.id.dialog_btn_add_pdt);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data_id = id.getText().toString().trim();
                String data_name = name.getText().toString().trim();
                String data_img = img.getText().toString().trim();

                // Kiểm tra nếu bất kỳ trường nào rỗng, không thêm dữ liệu
                if (TextUtils.isEmpty(data_id) || TextUtils.isEmpty(data_name) || TextUtils.isEmpty(data_img)) {
                    Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    // Nếu các trường không rỗng, thêm dữ liệu vào cơ sở dữ liệu Firebase
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product_Type");
                    String key = databaseReference.push().getKey();
                    databaseReference.child(key).child("Product_Type_Id").setValue(data_id);
                    databaseReference.child(key).child("Img_Product_Type").setValue(data_img);
                    databaseReference.child(key).child("Type_Name").setValue(data_name);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

}