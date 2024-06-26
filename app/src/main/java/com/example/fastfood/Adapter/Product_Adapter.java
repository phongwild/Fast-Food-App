package com.example.fastfood.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fastfood.Model.Product;
import com.example.fastfood.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Product_Adapter extends FirebaseRecyclerAdapter<Product,Product_Adapter.ViewHolder>{
    private Context context;
    public Product_Adapter(@NonNull FirebaseRecyclerOptions<Product> options, Context context) {
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Product model) {
        holder.tv_name_product.setText(model.getName());
        holder.tv_describe_product.setText(model.getDescribe());
        holder.tv_pdt_product.setText(model.getProduct_Type_Id());
        holder.tv_id_product.setText(model.getId());
        holder.tv_price_product.setText(String.format("%d",model.getPrice())+" đ");
        Glide.with(holder.img_product.getContext())
                .load(model.getImg_Product())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img_product);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(view.getContext());
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
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_update_product);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.style_dialog);
                TextInputEditText id, name, describe, price, type_id;
                EditText img;
                Button btn_update;
                id = dialog.findViewById(R.id.dialog_id_product_up);
                name = dialog.findViewById(R.id.dialog_name_product_up);
                describe = dialog.findViewById(R.id.dialog_describe_product_up);
                price = dialog.findViewById(R.id.dialog_price_product_up);
                img = dialog.findViewById(R.id.dialog_img_product_up);
                type_id = dialog.findViewById(R.id.dialog_typeId_product_up);
                btn_update = dialog.findViewById(R.id.dialog_btn_update);
                //get dữ liệu của item lên dialog
                id.setText(model.getId());
                name.setText(model.getName());
                describe.setText(model.getDescribe());
                price.setText(String.valueOf(model.getPrice()));
                img.setText(model.getImg_Product());
                type_id.setText(model.getProduct_Type_Id());
                btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String idd = id.getText().toString();
                        String namee = name.getText().toString();
                        String describee = describe.getText().toString();
                        int pricee = Integer.parseInt(price.getText().toString());
                        String imgg = img.getText().toString();
                        String type_idd = type_id.getText().toString();
                        //Lưu

                        int adapterPosition = holder.getAbsoluteAdapterPosition();
                        DatabaseReference itemRef = getRef(adapterPosition);

                        Map<String, Object> updateUser = new HashMap<>();
                        updateUser.put("Describe", describee);
                        updateUser.put("Img_Product", imgg);
                        updateUser.put("Name", namee);
                        updateUser.put("Price", pricee);
                        updateUser.put("Product_Type_Id", type_idd);

                        itemRef.updateChildren(updateUser)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        dialog.dismiss();
                                        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        dialog.dismiss();
                    }
                });
                dialog.show();
                return false;
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false);
        return new ViewHolder(v);
    }

    class ViewHolder extends  RecyclerView.ViewHolder{
        TextView tv_name_product, tv_describe_product, tv_price_product, tv_pdt_product, tv_id_product;
        ImageView img_product;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name_product = itemView.findViewById(R.id.name_item_product);
            tv_describe_product = itemView.findViewById(R.id.tv_describe_product);
            tv_price_product = itemView.findViewById(R.id.tv_price_product);
            tv_pdt_product = itemView.findViewById(R.id.tv_pdt_product);
            tv_id_product = itemView.findViewById(R.id.tv_id_product);
            img_product = itemView.findViewById(R.id.img_product);
        }
    }

}
