package com.example.fastfood.Fragment.Fragment_Admin;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fastfood.List_Activity.Activity_Login.Main_Login;
import com.example.fastfood.List_Activity.Main_Activity.Activity_changeInfo_admin;
import com.example.fastfood.List_Activity.Main_Activity.ChangePass_Activity_Admin;
import com.example.fastfood.List_Activity.Main_Activity.Changepassword_Activity;
import com.example.fastfood.List_Activity.Main_Activity.Statistical_Activity;
import com.example.fastfood.R;
import com.example.fastfood.databinding.FragmentAdminPersonalInformationBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Fragment_Admin_Personal_Information extends Fragment {
    public Fragment_Admin_Personal_Information() {

    }

    private FragmentAdminPersonalInformationBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminPersonalInformationBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        String savedUsername = getUsernameFromSharedPreferences();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
        userRef.orderByChild("user_Name").equalTo(savedUsername).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        String userId = snapshot1.getKey();
                        String name = snapshot1.child("full_Name").getValue(String.class);
                        String avt = snapshot1.child("user_Image").getValue(String.class);
                        binding.FullNameAdminInformation.setText(name);
                        Picasso.get().load(avt).into(binding.ImageAdmin);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.LogoutAdminInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
        binding.ChangepasswordAdminInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChangePass_Activity_Admin.class));
            }
        });

        binding.StatisticalAdminInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Statistical_Activity.class));
            }
        });
        binding.ChangeinformationAdminInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_changeInfo_admin.class));
            }
        });
        return view;
    }

    private void logout(){
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("loginStatus", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        startActivity(new Intent(getActivity(), Main_Login.class));
    }
    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("loginStatus", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("username", ""); // "" là giá trị mặc định nếu không tìm thấy username trong SharedPreferences
    }
}