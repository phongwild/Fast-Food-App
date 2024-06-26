package com.example.fastfood.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fastfood.Model.User;
import com.example.fastfood.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_Adapter extends FirebaseRecyclerAdapter<User, User_Adapter.myViewHolder> {
    private Context context;
    public User_Adapter(@NonNull FirebaseRecyclerOptions<User> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull User model) {
        holder.User_FullName.setText(model.getFull_Name());
        holder.User_Id.setText(model.getUser_Id());
        holder.User_Account.setText(model.getUser_Name());
        String roleString = (model.getRole() == 0) ? "Admin" : "User";
        holder.User_Role.setText(roleString);
        holder.User_Email.setText(model.getEmail());
        holder.User_Address.setText(model.getAddress());
        holder.User_PhoneNumber.setText("0" + String.valueOf(model.getPhone_Number()));
        Glide.with(holder.Image_User.getContext())
                .load(model.getUser_Image())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.Image_User);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Bạn có chắc muốn xóa người dùng này chứ!")
                        .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int adapterPosition = holder.getAbsoluteAdapterPosition();
                                DatabaseReference itemRef = getRef(adapterPosition);
                                itemRef.removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                // Xóa thành công trong Firebase, giờ cập nhật dataset local
                                                notifyItemRemoved(adapterPosition);
                                                notifyItemRangeChanged(adapterPosition, getItemCount());
                                                Toast.makeText(view.getContext(), "Delete thành công", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(view.getContext(), "Delete thất bại", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }).setNegativeButton("Hủy bỏ", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.update_user))
                        .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT) // Đặt chiều rộng của nội dung
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT) // Đặt chiều cao của nội dung
                        .create();

                View view1 = dialogPlus.getHolderView();
                EditText add_Image_User = view1.findViewById(R.id.add_Image_User);
                EditText add_Account_User = view1.findViewById(R.id.add_Account_User);
                EditText add_PassWord_User = view1.findViewById(R.id.add_PassWord_User);
                EditText add_Role_User = view1.findViewById(R.id.add_Role_User);
                EditText add_Email_User = view1.findViewById(R.id.add_Email_User);
                EditText add_FullName_User = view1.findViewById(R.id.add_FullName_User);
                EditText add_Address_User = view1.findViewById(R.id.add_Address_User);
                EditText add_PhoneNumber_User = view1.findViewById(R.id.add_PhoneNumber_User);
                Button btnupdate = view1.findViewById(R.id.add_User);

                User selectedUser = getItem(position);

                add_Image_User.setText(selectedUser.getUser_Image());
                add_Account_User.setText(selectedUser.getUser_Name());
                add_PassWord_User.setText(selectedUser.getPass_Word());
                add_Role_User.setText(String.valueOf(selectedUser.getRole()));
                add_Email_User.setText(selectedUser.getEmail());
                add_FullName_User.setText(selectedUser.getFull_Name());
                add_Address_User.setText(selectedUser.getAddress());
                add_PhoneNumber_User.setText(String.valueOf(selectedUser.getPhone_Number()));

                btnupdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String UpdateImg = add_Image_User.getText().toString().trim();
                        String UpdateAccount = add_Account_User.getText().toString().trim();
                        String UpdatePassword = add_PassWord_User.getText().toString().trim();
                        String UpdateRoleStr = add_Role_User.getText().toString().trim();
                        String UpdateEmail = add_Email_User.getText().toString().trim();
                        String UpdateFullName = add_FullName_User.getText().toString().trim();
                        String UpdateAddress = add_Address_User.getText().toString().trim();
                        String UpdatePhoneNumberStr = add_PhoneNumber_User.getText().toString().trim();

                        // Validate các trường
                        if (UpdateAccount.isEmpty() || UpdatePassword.isEmpty() || UpdateRoleStr.isEmpty() || UpdateEmail.isEmpty() || UpdateFullName.isEmpty() || UpdateAddress.isEmpty() || UpdatePhoneNumberStr.isEmpty()) {
                            if (UpdateAccount.isEmpty()) {
                                add_Account_User.setError("Vui lòng nhập tên tài khoản");
                                return;
                            }
                            if (UpdatePassword.isEmpty()) {
                                add_PassWord_User.setError("Vui lòng nhập mật khẩu");
                                return;
                            }
                            if (UpdateRoleStr.isEmpty()) {
                                add_Role_User.setError("Vui lòng nhập vai trò");
                                return;
                            }
                            if (UpdateEmail.isEmpty()) {
                                add_Email_User.setError("Vui lòng nhập địa chỉ email");
                                return;
                            }
                            if (UpdateFullName.isEmpty()) {
                                add_FullName_User.setError("Vui lòng nhập họ và tên");
                                return;
                            }
                            if (UpdateAddress.isEmpty()) {
                                add_Address_User.setError("Vui lòng nhập địa chỉ");
                                return;
                            }
                            if (UpdatePhoneNumberStr.isEmpty()) {
                                add_PhoneNumber_User.setError("Vui lòng nhập số điện thoại");
                                return;
                            }
                        }

                        // Kiểm tra Role là số và nằm trong khoảng 0 - 1, hiển thị lỗi khi không đúng
                        int UpdateRole;
                        try {
                            UpdateRole = Integer.parseInt(UpdateRoleStr);
                            if (UpdateRole < 0 || UpdateRole > 1) {
                                add_Role_User.setError("Role phải nằm trong khoảng từ 0 đến 1");
                                return;
                            }
                        } catch (NumberFormatException e) {
                            add_Role_User.setError("Role phải là số");
                            return;
                        }

                        // Kiểm tra và chuyển đổi PhoneNumber từ String sang Long, hiển thị lỗi khi không hợp lệ
                        long UpdatePhoneNumber;
                        try {
                            UpdatePhoneNumber = Long.parseLong(UpdatePhoneNumberStr);
                            if (UpdatePhoneNumber < 0) {
                                throw new NumberFormatException();
                            }
                        } catch (NumberFormatException e) {
                            add_PhoneNumber_User.setError("Số điện thoại không hợp lệ");
                            return;
                        }

                        // Kiểm tra Email có đúng định dạng, hiển thị lỗi khi không đúng
                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(UpdateEmail).matches()) {
                            add_Email_User.setError("Địa chỉ email không đúng định dạng");
                            return;
                        }

                        // Tiến hành cập nhật nếu thông tin hợp lệ
                        int adapterPosition = holder.getAbsoluteAdapterPosition();
                        DatabaseReference itemRef = getRef(adapterPosition);

                        Map<String, Object> updateUser = new HashMap<>();
                        updateUser.put("user_Image", UpdateImg);
                        updateUser.put("user_Name", UpdateAccount);
                        updateUser.put("pass_Word", UpdatePassword);
                        updateUser.put("role", UpdateRole);
                        updateUser.put("email", UpdateEmail);
                        updateUser.put("full_Name", UpdateFullName);
                        updateUser.put("address", UpdateAddress);
                        updateUser.put("phone_Number", UpdatePhoneNumber);

                        itemRef.updateChildren(updateUser)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialogPlus.dismiss();
                                        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                dialogPlus.show();
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_manager,parent,false);
        return new myViewHolder(view);
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView Image_User;
        TextView User_FullName, User_Id, User_Account, User_Role, User_Email, User_Address, User_PhoneNumber;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            Image_User = itemView.findViewById(R.id.Image_User);
            User_FullName = itemView.findViewById(R.id.User_FullName);
            User_Id = itemView.findViewById(R.id.User_Id);
            User_Account = itemView.findViewById(R.id.User_Account);
            User_Role = itemView.findViewById(R.id.User_Role);
            User_Email = itemView.findViewById(R.id.User_Email);
            User_Address = itemView.findViewById(R.id.User_Address);
            User_PhoneNumber = itemView.findViewById(R.id.User_PhoneNumber);
        }
    }
}
