package com.example.fastfood.Fragment.Fragment_User;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fastfood.Adapter.Cart_Adapter;
import com.example.fastfood.Adapter.Rcv2_adapter;
import com.example.fastfood.List_Activity.Main_Activity.oderDeatil;
import com.example.fastfood.Model.Cart;
import com.example.fastfood.Model.Product;
import com.example.fastfood.databinding.FragmentProductBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_User_Cart extends Fragment{
    public Fragment_User_Cart() {

    }

    private String userId;

    private FragmentProductBinding binding;
    Cart_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        userId = getUserIdFromSharedPreferences();

        binding.rcvCart.setLayoutManager(new LinearLayoutManager(getActivity()));
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(FirebaseDatabase.getInstance().getReference()
                                .child("Cart")
                                .orderByChild("user_Id")
                                .equalTo(userId), Cart.class)
                        .build();
        adapter = new Cart_Adapter(options);
        binding.rcvCart.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        binding.orderCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter.getItemCount() > 0) {
                    startActivity(new Intent(getActivity(), oderDeatil.class));
                } else {
                    Toast.makeText(getActivity(), "Giỏ hàng của bạn đang trống", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    private String getUserIdFromSharedPreferences(){
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("userId","");
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}