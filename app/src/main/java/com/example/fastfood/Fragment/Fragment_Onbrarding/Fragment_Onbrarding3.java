package com.example.fastfood.Fragment.Fragment_Onbrarding;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fastfood.List_Activity.Activity_Login.Main_Login;
import com.example.fastfood.R;

public class Fragment_Onbrarding3 extends Fragment {
    public Fragment_Onbrarding3() {
        // Required empty public constructor
    }

    private Button btn_Start;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__onbrarding3, container, false);
        btn_Start = view.findViewById(R.id.Onbroarding_Start);

        btn_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Main_Login.class));
            }
        });
        return view;
    }
}