package com.example.fastfood.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fastfood.Model.Cart;
import com.example.fastfood.Model.Product;
import com.example.fastfood.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Locale;

public class oderDeltai_Adapter extends FirebaseRecyclerAdapter<Cart, oderDeltai_Adapter.myViewHolder> {
    private int totalCost = 0;
    private OnDataClickListener dataClickListener;
    public interface OnDataClickListener{
        void onDataClicked(String productId, int total, int price, int quantity, int totalCost);
    }
    public void setOnDataClickListener(OnDataClickListener dataClickListener) {
        this.dataClickListener = dataClickListener;
    }
    public oderDeltai_Adapter(@NonNull FirebaseRecyclerOptions<Cart> options) {
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Cart model) {
        String productId = model.getId();
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Product");
        Query query = productRef.orderByChild("id").equalTo(productId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Product product = dataSnapshot.getValue(Product.class);
                        holder.name_oderDealtai.setText(product.getName());
                        holder.price_oderDealtai.setText(String.valueOf(product.getPrice()));
                        Glide.with(holder.Img_oderDealtai.getContext())
                                .load(product.getImg_Product())
                                .into(holder.Img_oderDealtai);
                        holder.quantity_oderDealtai.setText(String.valueOf(model.getCart_quantity()));

                        int price = product.getPrice();
                        int quantity = model.getCart_quantity();

                        int Total = price * quantity;
                        totalCost += Total;

                        if(dataClickListener != null){
                            dataClickListener.onDataClicked(productId,Total,price,quantity,totalCost);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_oderdeltai, parent, false);
        return new myViewHolder(view);
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        ImageView Img_oderDealtai;
        TextView name_oderDealtai, price_oderDealtai, quantity_oderDealtai;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            Img_oderDealtai = itemView.findViewById(R.id.Img_oderDealtai);
            name_oderDealtai = itemView.findViewById(R.id.name_oderDealtai);
            price_oderDealtai = itemView.findViewById(R.id.price_oderDealtai);
            quantity_oderDealtai = itemView.findViewById(R.id.quantity_oderDealtai);
        }
    }
}
