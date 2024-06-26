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
import com.example.fastfood.Model.Bill_Detail;
import com.example.fastfood.Model.Cart;
import com.example.fastfood.Model.Product;
import com.example.fastfood.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class History_adapter extends FirebaseRecyclerAdapter<Bill_Detail, History_adapter.myViewHolder> {
    private String userId;
    private Context context;
    public History_adapter(@NonNull FirebaseRecyclerOptions<Bill_Detail> options, Context context) {
        super(options);
        this.context = context;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Bill_Detail model) {
        holder.Billdetailid_history.setText(model.getInvoice_detail_id());
        holder.Quantity_history.setText(String.valueOf(model.getCart_quantity()));
        holder.Price_history.setText(String.valueOf(model.getPrice()) + " VNĐ");
        holder.TotalDetail_history.setText(String.valueOf(model.getTotal_details()) + " VNĐ");
        String productId = model.getId();
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Product");
        Query query = productRef.orderByChild("id").equalTo(productId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Product product = dataSnapshot.getValue(Product.class);
                        holder.Nameproduct_history.setText(product.getName());
                        Glide.with(holder.image_history.getContext())
                                .load(product.getImg_Product())
                                .into(holder.image_history);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.btn_order_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart(model);
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
        return new myViewHolder(v);
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        ImageView image_history;
        TextView Billdetailid_history, Nameproduct_history, Quantity_history, Price_history, TotalDetail_history;
        Button btn_order_history;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            image_history = itemView.findViewById(R.id.image_history);
            Billdetailid_history = itemView.findViewById(R.id.Billdetailid_history);
            Nameproduct_history = itemView.findViewById(R.id.Nameproduct_history);
            Quantity_history = itemView.findViewById(R.id.Quantity_history);
            Price_history = itemView.findViewById(R.id.Price_history);
            TotalDetail_history = itemView.findViewById(R.id.TotalDetail_history);
            btn_order_history = itemView.findViewById(R.id.btn_reOrder_history);
        }
    }
    private void addToCart(Bill_Detail billDetail){
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Cart").push();
        String cartId = UUID.randomUUID().toString();

        Cart cartItem = new Cart();

        cartItem.setCart_Id(cartId);
        cartItem.setUser_Id(userId);
        cartItem.setId(billDetail.getId());
        cartItem.setCart_quantity(1);
        cartRef.setValue(cartItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Thêm sản phẩm vào lại giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Thêm sản phẩm vào giỏ hàng không thành công", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
