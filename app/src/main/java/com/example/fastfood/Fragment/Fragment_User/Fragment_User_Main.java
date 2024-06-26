package com.example.fastfood.Fragment.Fragment_User;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.example.fastfood.Adapter.Rcv1_adapter;
import com.example.fastfood.Adapter.Rcv2_adapter;
import com.example.fastfood.Adapter.Rcv3_adapter;
import com.example.fastfood.Model.Product;
import com.example.fastfood.Model.ProductType;
import com.example.fastfood.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_User_Main extends Fragment implements Rcv1_adapter.OnItemClickListener{
    private ViewFlipper viewFlipper;
    RecyclerView rcv1, rcv2, rcv3;
    Rcv1_adapter adapter;
    Rcv2_adapter adapter2;
    Rcv3_adapter adapter3;
    SearchView sv_User;
    public Fragment_User_Main() {}
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment__main, container, false);

        String savedUsername = getUsernameFromSharedPreferences();
        Log.d("Userlogin", "lấy dữ liệu thành công: " + savedUsername);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
        userRef.orderByChild("user_Name").equalTo(savedUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        String userId = snapshot1.getKey();
                        Log.d("UserIdMain", "onDataChange: " + userId);
                        String fullname = snapshot1.child("full_Name").getValue(String.class);
                        String address = snapshot1.child("address").getValue(String.class);
                        int phonenumber = snapshot1.child("phone_Number").getValue(Integer.class);
                        saveUserIdToSharedPreferences(userId,phonenumber,fullname,address);

                        adapter2.setUserId(userId);
                        adapter3.setUserId(userId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Slider
        viewFlipper = v.findViewById(R.id.slider);
            //img1
        ImageView img1 = new ImageView(getActivity());
        img1.setImageResource(R.drawable.img_slider_1);
        viewFlipper.addView(img1);
            //img2
        ImageView img2 = new ImageView(getActivity());
        img2.setImageResource(R.drawable.img_slider_2);
        viewFlipper.addView(img2);
            //img3
        ImageView img3 = new ImageView(getActivity());
        img3.setImageResource(R.drawable.img_slider_3);
        viewFlipper.addView(img3);

        //

        rcv1 = v.findViewById(R.id.rcv1_frm_main_user);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rcv1.setLayoutManager(manager);
        FirebaseRecyclerOptions<ProductType> options =
                new FirebaseRecyclerOptions.Builder<ProductType>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Product_Type"),ProductType.class)
                        .build();
        adapter = new Rcv1_adapter(options);
        adapter.setOnItemClickListener(this);
        rcv1.setAdapter(adapter);

        //

        rcv2 = v.findViewById(R.id.rcv2_frm_main_user);
        LinearLayoutManager manager2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        rcv2.setLayoutManager(manager2);
        FirebaseRecyclerOptions<Product> options2 =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Product"),Product.class)
                        .build();
        adapter2 = new Rcv2_adapter(options2, getActivity());
        rcv2.setAdapter(adapter2);
        //
        rcv3 = v.findViewById(R.id.rcv3_frm_main_user);
        rcv3.setLayoutManager(new LinearLayoutManager(getActivity()));
        FirebaseRecyclerOptions<Product> options3 =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Product"),Product.class)
                        .build();
        adapter3 = new Rcv3_adapter(options3,getActivity());
        rcv3.setAdapter(adapter3);
        //search
        sv_User = v.findViewById(R.id.sv_frm_main_user);
        return v;
        //
    }

    @Override
    public void onItemClick(String productTypeId) {
        FirebaseRecyclerOptions<Product> optionsForRcv2 =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("Product")
                                .orderByChild("Product_Type_Id")
                                .equalTo(productTypeId), Product.class) // Thêm phương thức equalTo() để so sánh với productTypeId
                        .build();
        adapter2.updateOptions(optionsForRcv2);
        adapter2.notifyDataSetChanged();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewFlipper.startFlipping();
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        adapter3.startListening();
        adapter2.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
        adapter2.stopListening();
        adapter3.stopListening();
    }

    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("loginStatus", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("username", ""); // "" là giá trị mặc định nếu không tìm thấy username trong SharedPreferences
    }

    private void saveUserIdToSharedPreferences(String userId, int phoneNumber, String fullName, String address){
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", userId);
        editor.putInt("phoneNumber", phoneNumber);
        editor.putString("fullName", fullName);
        editor.putString("address", address);
        editor.apply();
    }

}