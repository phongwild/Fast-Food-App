package com.example.fastfood.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.fastfood.List_Activity.Activity_Management.Product_Manager;
import com.example.fastfood.Model.Product;
import com.example.fastfood.Model.ProductType;
import com.example.fastfood.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Product_Type_Adapter extends FirebaseRecyclerAdapter<ProductType, Product_Type_Adapter.ViewHolder> {

    public Product_Type_Adapter(@NonNull FirebaseRecyclerOptions<ProductType> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ProductType model) {
        holder.tv_id_item_product_type.setText(model.getProduct_Type_Id());
        holder.tv_name_item_product_type.setText(model.getType_Name());
        Glide.with(holder.img_product_type.getContext())
                .load(model.getImg_Product_Type())
                .placeholder(com.google.android.gms.base.R.drawable.common_google_signin_btn_icon_dark)
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .circleCrop()
                .into(holder.img_product_type);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productTypeId = model.getProduct_Type_Id();
                Intent intent = new Intent(view.getContext(), Product_Manager.class);
                intent.putExtra("Product_Type_Id", productTypeId);
                view.getContext().startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
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
                return false;
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_type, parent, false);
        return new ViewHolder(v);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_id_item_product_type, tv_name_item_product_type;
        ImageView img_product_type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_product_type = itemView.findViewById(R.id.img_product_type);
            tv_id_item_product_type = itemView.findViewById(R.id.tv_id_product_type);
            tv_name_item_product_type = itemView.findViewById(R.id.tv_name_product_type);
        }
    }

}
