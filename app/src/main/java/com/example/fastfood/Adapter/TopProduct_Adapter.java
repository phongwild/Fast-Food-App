package com.example.fastfood.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fastfood.Model.Bill_Detail;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TopProduct_Adapter extends RecyclerView.Adapter<TopProduct_Adapter.myViewHolder> {
    private List<Bill_Detail> billDetails;

    public TopProduct_Adapter(List<Bill_Detail> billDetails) {
        this.billDetails = billDetails;
    }

    // Override các phương thức cần thiết của RecyclerView.Adapter

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topproduct, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Bill_Detail model = billDetails.get(position);
        holder.quantity_topProduct.setText(String.valueOf(model.getCart_quantity()));
        double total = model.getTotal_details();
        String formattedTotal = NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(total);
        holder.total_topProduct.setText(formattedTotal);

        String productId = model.getId();
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Product");
        Query query = productRef.orderByChild("id").equalTo(productId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Product product = dataSnapshot.getValue(Product.class);
                        holder.name_topProduct.setText(product.getName());
                        Glide.with(holder.Image_topProduct.getContext())
                                .load(product.getImg_Product())
                                .into(holder.Image_topProduct);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi
            }
        });
    }

    @Override
    public int getItemCount() {
        return billDetails.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        ImageView Image_topProduct;
        TextView name_topProduct, quantity_topProduct, total_topProduct;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            Image_topProduct = itemView.findViewById(R.id.Image_topProduct);
            name_topProduct = itemView.findViewById(R.id.name_topProduct);
            quantity_topProduct = itemView.findViewById(R.id.quantity_topProduct);
            total_topProduct = itemView.findViewById(R.id.total_topProduct);
        }
    }
}

