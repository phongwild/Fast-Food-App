package com.example.fastfood.Fragment.Fragment_Login_Account;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fastfood.List_Activity.Activity_Login.Enter_Information;
import com.example.fastfood.R;
import com.example.fastfood.databinding.FragmentCreateAccountBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Create_Account extends Fragment {
    public Fragment_Create_Account() {
    }

    private FragmentCreateAccountBinding binding;
    float v = 0;

    String username, password, repassword ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateAccountBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        animation_login();
        // xử lý đăng ký tài khoản
        binding.edCreateUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0){
                    binding.inCreateUserName.setError("Vui lòng nhập username");
                }else{
                    binding.inCreateUserName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.edCreatePassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0){
                    binding.inCreatePassWord.setError("Vui lòng nhập mật khẩu");
                }else{
                    binding.inCreatePassWord.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.edCreateRePassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0){
                    binding.inCreateRePassWord.setError("Vui lòng nhập mật khẩu");
                }else{
                    binding.inCreateRePassWord.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("User");

        try {
            binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    username = binding.edCreateUserName.getText().toString();
                    password = binding.edCreatePassWord.getText().toString();
                    repassword = binding.edCreateRePassWord.getText().toString();

                    // Kiểm tra username nếu không rỗng
                    if (!TextUtils.isEmpty(username)) {
                        Query usernameQuery = userRef.orderByChild("user_Name").equalTo(username);
                        usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    binding.inCreateUserName.setError("Tài khoản đã tồn tại, vui lòng nhập tài khoản khác!");
                                } else {
                                    checkOtherFields();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        binding.inCreateUserName.setError("Vui lòng không để trống Username");
                    }
                }
            });
        }catch (Exception e){
            Log.e("Linked database false", e.toString());
        }


        return view;
    }

    private void checkOtherFields() {
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(repassword)) {
            // Kiểm tra các trường nhập liệu khác nếu cần thiết
        } else if (!password.equals(repassword)) {
            binding.inCreateRePassWord.setError("Repass phải trùng khớp với Password");
        } else if (password.length() < 10) {
            binding.inCreatePassWord.setError("Password phải có ít nhất 10 ký tự");
        } else {
            // Nếu tất cả các điều kiện đều đúng, tiến hành đăng ký hoặc chuyển màn hình
            Intent intent = new Intent(getActivity(), Enter_Information.class);
            Bundle bundle = new Bundle();
            bundle.putString("Username", username);
            bundle.putString("Password", password);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        // Giải phóng binding khi Fragment bị hủy
        binding = null;
    }

    private void animation_login(){
        binding.inCreateUserName.setTranslationX(800);
        binding.inCreatePassWord.setTranslationX(800);
        binding.inCreateRePassWord.setTranslationX(800);
        binding.btnSignIn.setTranslationX(800);
        binding.footer.setTranslationY(300);

        binding.inCreateUserName.setAlpha(v);
        binding.inCreatePassWord.setAlpha(v);
        binding.inCreateRePassWord.setAlpha(v);
        binding.btnSignIn.setAlpha(v);
        binding.footer.setAlpha(v);

        binding.inCreateUserName.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        binding.inCreatePassWord.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        binding.inCreateRePassWord.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        binding.btnSignIn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        binding.footer.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(700).start();
    }
}