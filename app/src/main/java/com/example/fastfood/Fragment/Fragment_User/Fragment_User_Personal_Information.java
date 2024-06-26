package com.example.fastfood.Fragment.Fragment_User;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fastfood.List_Activity.Activity_Login.Main_Login;
import com.example.fastfood.List_Activity.Main_Activity.Activity_changeInfo;
import com.example.fastfood.List_Activity.Main_Activity.Activity_history;
import com.example.fastfood.List_Activity.Main_Activity.Bill_History;
import com.example.fastfood.List_Activity.Main_Activity.Changepassword_Activity;
import com.example.fastfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_User_Personal_Information extends Fragment {
    CardView btn_logout_user, btn_changePass, btn_changeInfo, btn_bill_user;
    CardView btn_history;
    CircleImageView avt_user;
    TextView name_user;
    public Fragment_User_Personal_Information() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment__personal__information, container, false);
        btn_logout_user = v.findViewById(R.id.btn_logOut_user);
        btn_changePass = v.findViewById(R.id.btn_change_pass_user);
        btn_changeInfo = v.findViewById(R.id.btn_change_info_user);
        btn_bill_user = v.findViewById(R.id.btn_bill_user);
        btn_history = v.findViewById(R.id.btn_history_user);
        avt_user = v.findViewById(R.id.img_user);
        name_user = v.findViewById(R.id.name_user);
        String savedUsername = getUsernameFromSharedPreferences();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");
        userRef.orderByChild("user_Name").equalTo(savedUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        String userId = snapshot1.getKey();
                        String name = snapshot1.child("full_Name").getValue(String.class);
                        String avt = snapshot1.child("user_Image").getValue(String.class);
                        name_user.setText(name);
                        Picasso.get().load(avt).into(avt_user);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btn_changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Changepassword_Activity.class);
                startActivity(intent);
            }
        });
        btn_changeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_changeInfo.class));
            }
        });
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Activity_history.class));
            }
        });
        btn_logout_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Logout", Toast.LENGTH_SHORT).show();
                logout();
            }
        });
        btn_bill_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Bill_History.class));
            }
        });
        return v;
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