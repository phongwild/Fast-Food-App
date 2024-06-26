package com.example.fastfood.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fastfood.Model.Cart;
import com.example.fastfood.Model.Product;
import com.example.fastfood.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;


public class Rcv3_adapter extends FirebaseRecyclerAdapter<Product,Rcv3_adapter.ViewHolder> {
    private Context context;

    private String userId;

    public void setUserId(String userId){
        this.userId = userId;
    }
    public Rcv3_adapter(@NonNull FirebaseRecyclerOptions<Product> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Product model) {
        Log.d("rcv3", "Binding data for position: " +  model.getId());
        holder.tv_rcv3_id.setText(model.getId());
        holder.tv_rcv3_describe.setText(model.getDescribe());
        holder.tv_rcv3_price.setText(String.valueOf(model.getPrice()) + " đ");
        holder.tv_rcv3_typeId.setText(model.getProduct_Type_Id());
        holder.tv_rcv3_name.setText(model.getName());
        Glide.with(holder.img_rcv3.getContext())
                .load(model.getImg_Product())
                .into(holder.img_rcv3);

        holder.btn_add_rcv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart(model);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv3_main_user,parent,false);
        return new ViewHolder(v);
    }

    class ViewHolder extends  RecyclerView.ViewHolder{
        TextView tv_rcv3_name, tv_rcv3_price, tv_rcv3_id, tv_rcv3_describe, tv_rcv3_typeId;
        ImageView img_rcv3;
        Button btn_add_rcv3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_rcv3_name = itemView.findViewById(R.id.item_name_rcv3);
            img_rcv3 = itemView.findViewById(R.id.item_img_rcv3);
            tv_rcv3_price = itemView.findViewById(R.id.item_price_rcv3);
            btn_add_rcv3 = itemView.findViewById(R.id.item_btnAdd_rcv3);
            tv_rcv3_id = itemView.findViewById(R.id.item_id_rcv3);
            tv_rcv3_describe = itemView.findViewById(R.id.item_des_rcv3);
            tv_rcv3_typeId = itemView.findViewById(R.id.item_type_rcv3);
        }
    }

    private void addToCart(Product product){
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart").push();
        String cartId = UUID.randomUUID().toString();

        Cart cartItem = new Cart();

        cartItem.setCart_Id(cartId);
        cartItem.setUser_Id(userId);
        cartItem.setId(product.getId());
        cartItem.setCart_quantity(1);
        cartRef.setValue(cartItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Thêm sản phẩm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("addToCart", "Thêm sản phẩm vào giỏ hàng không thành công: " + e.getMessage());
                        Toast.makeText(context, "Thêm sản phẩm vào giỏ hàng không thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
